import { Client } from "@stomp/stompjs";
import { BehaviorSubject, Observable } from "rxjs";

export interface WebSocketMessage {
  type: string;
  data: any;
  timestamp: Date;
}

class WebSocketService {
  private client: Client | null = null;
  private messageSubject = new BehaviorSubject<WebSocketMessage | null>(null);
  private connectionSubject = new BehaviorSubject<boolean>(false);
  private reconnectAttempts = 0;
  private maxReconnectAttempts = 5;

  connect(url: string) {
    if (this.client?.connected) {
      console.log("WebSocket já está conectado");
      return;
    }

    try {
      // Criar cliente STOMP com WebSocket nativo
      this.client = new Client({
        // URL WebSocket nativo
        brokerURL: url,

        debug: (str: string) => {
          if (import.meta.env.DEV) {
            console.log("STOMP:", str);
          }
        },

        // Configurações de reconexão e heartbeat
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,

        // Callback quando conectar
        onConnect: () => {
          console.log("WebSocket conectado");
          this.connectionSubject.next(true);
          this.reconnectAttempts = 0;

          this.client?.subscribe("/topic/notifications", (message) => {
            try {
              const data = JSON.parse(message.body);
              console.log("Notificação recebida:", data);
              this.messageSubject.next(data);
            } catch (error) {
              console.error("Erro ao processar mensagem:", error);
            }
          });
        },

        // Callback quando desconectar
        onDisconnect: () => {
          console.log("WebSocket desconectado");
          this.connectionSubject.next(false);
        },

        // Callback de erro
        onStompError: (frame) => {
          console.error("Erro STOMP:", frame.headers["message"]);
          console.error("Detalhes:", frame.body);
          this.connectionSubject.next(false);
        },

        // Callback de erro de WebSocket
        onWebSocketError: (event) => {
          console.error("Erro WebSocket:", event);
        },
      });

      // Ativar cliente
      console.log("Conectando ao WebSocket:", url);
      this.client.activate();
    } catch (error) {
      console.error("Erro ao criar cliente WebSocket:", error);
      this.connectionSubject.next(false);
    }
  }

  disconnect() {
    if (this.client) {
      console.log("Desconectando WebSocket...");
      try {
        this.client.deactivate();
      } catch (error) {
        console.warn("Erro ao desconectar:", error);
      }
      this.client = null;
    }
    this.connectionSubject.next(false);
  }

  send(destination: string, message: any) {
    if (this.client?.connected) {
      this.client.publish({
        destination,
        body: JSON.stringify(message),
      });
      console.log("Mensagem enviada:", destination, message);
    } else {
      console.warn("WebSocket não está conectado. Mensagem não enviada.");
    }
  }

  getMessages(): Observable<WebSocketMessage | null> {
    return this.messageSubject.asObservable();
  }

  getConnectionStatus(): Observable<boolean> {
    return this.connectionSubject.asObservable();
  }

  isConnected(): boolean {
    return this.client?.connected || false;
  }
}

export const webSocketService = new WebSocketService();
