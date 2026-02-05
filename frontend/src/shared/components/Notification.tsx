import { useEffect, useState } from "react";
import { useObservable } from "../hooks/useObservable";
import { webSocketService } from "../services/websocket";

export default function Notification() {
  const message = useObservable(webSocketService.getMessages(), null);
  const [show, setShow] = useState(false);

  useEffect(() => {
    if (message && message.type === "NEW_ALBUM") {
      setShow(true);
      setTimeout(() => setShow(false), 5000);
    }
  }, [message]);

  if (!show || !message) return null;

  return (
    <div className="fixed top-4 right-4 z-50 animate-slide-in-right">
      <div className="bg-spotiplag-blue rounded-lg shadow-2xl max-w-sm overflow-hidden">
        <div className="flex items-start gap-4 p-4">
          {/* Album Icon */}
          <div className="flex-shrink-0 w-16 h-16 bg-spotiplag-black/20 rounded flex items-center justify-center">
            <svg
              className="w-8 h-8 text-spotiplag-black"
              fill="currentColor"
              viewBox="0 0 24 24"
            >
              <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 14.5c-2.49 0-4.5-2.01-4.5-4.5S9.51 7.5 12 7.5s4.5 2.01 4.5 4.5-2.01 4.5-4.5 4.5zm0-5.5c-.55 0-1 .45-1 1s.45 1 1 1 1-.45 1-1-.45-1-1-1z" />
            </svg>
          </div>

          {/* Content */}
          <div className="flex-1 min-w-0">
            <h4 className="font-bold text-spotiplag-black text-lg">
              Novo Álbum!
            </h4>
            <p className="text-spotiplag-black/80 text-sm mt-1 truncate">
              {message.data?.title || "Um novo álbum foi adicionado"}
            </p>
          </div>

          {/* Close Button */}
          <button
            onClick={() => setShow(false)}
            className="flex-shrink-0 text-spotiplag-black/60 hover:text-spotiplag-black transition-colors"
          >
            <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
              <path
                fillRule="evenodd"
                d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                clipRule="evenodd"
              />
            </svg>
          </button>
        </div>
      </div>
    </div>
  );
}
