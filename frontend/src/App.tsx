import { BrowserRouter } from "react-router-dom";
import { useEffect } from "react";
import AppRoutes from "./app/routes/AppRoutes";
import { webSocketService } from "./shared/services/websocket";
import { useTokenRefresh } from "./shared/hooks/useTokenRefresh";

function App() {
  useTokenRefresh();
  useEffect(() => {
    const wsUrl = import.meta.env.VITE_WS_URL || "ws://localhost:8080/ws";

    console.log("Iniciando aplicação SpotiPLAG...");
    console.log("Conectando ao WebSocket:", wsUrl);

    try {
      webSocketService.connect(wsUrl);
    } catch (error) {
      console.warn("Erro ao conectar WebSocket (não crítico):", error);
    }

    return () => {
      console.log("Aplicação sendo encerrada...");
      webSocketService.disconnect();
    };
  }, []);

  return (
    <BrowserRouter>
      <AppRoutes />
    </BrowserRouter>
  );
}

export default App;
