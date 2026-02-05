import { useEffect } from "react";
import { useParams, Link, useNavigate } from "react-router-dom";
import { useObservable } from "../../../shared/hooks/useObservable";
import artistFacade from "../ArtistFacade";
import LoadingSpinner from "../../../shared/components/LoadingSpinner";

export default function ArtistDetail() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const artistState = useObservable(
    artistFacade.getState(),
    artistFacade.getCurrentState(),
  );

  const { selectedArtist: artist, loading, error } = artistState;

  useEffect(() => {
    if (id) {
      artistFacade.loadArtistById(Number(id));
    }
    return () => {
      artistFacade.clearSelectedArtist();
    };
  }, [id]);

  if (loading) {
    return <LoadingSpinner />;
  }

  if (error) {
    return (
      <div className="p-8">
        <div className="bg-red-500/10 border border-red-500 text-red-500 px-4 py-3 rounded-md">
          {error}
        </div>
      </div>
    );
  }

  if (!artist) {
    return (
      <div className="p-8 text-center py-20">
        <svg
          className="w-24 h-24 text-spotiplag-darkwhite mx-auto mb-4"
          fill="currentColor"
          viewBox="0 0 20 20"
        >
          <path
            fillRule="evenodd"
            d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z"
            clipRule="evenodd"
          />
        </svg>
        <p className="text-spotiplag-lightwhite text-xl mb-4">
          Artista não encontrado.
        </p>
        <Link to="/artists" className="btn-primary">
          Voltar para Artistas
        </Link>
      </div>
    );
  }

  return (
    <div>
      {/* Header Section com Gradiente */}
      <div className="relative bg-gradient-to-b from-spotiplag-blue/20 to-transparent">
        <div className="absolute inset-0 bg-gradient-to-b from-black/40 to-transparent"></div>

        <div className="relative p-8">
          {/* Breadcrumb */}
          <nav className="mb-6 text-sm flex items-center gap-2">
            <Link
              to="/artists"
              className="text-spotiplag-lightwhite hover:text-spotiplag-white transition-colors"
            >
              Artistas
            </Link>
            <svg
              className="w-4 h-4 text-spotiplag-lightwhite"
              fill="currentColor"
              viewBox="0 0 20 20"
            >
              <path
                fillRule="evenodd"
                d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z"
                clipRule="evenodd"
              />
            </svg>
            <span className="text-spotiplag-white font-semibold">
              {artist.name}
            </span>
          </nav>

          {/* Artist Header */}
          <div className="flex items-end gap-6">
            <div className="flex-shrink-0">
              <div className="w-60 h-60 bg-spotiplag-darkgray rounded-full flex items-center justify-center shadow-2xl">
                <svg
                  className="w-32 h-32 text-spotiplag-darkwhite"
                  fill="currentColor"
                  viewBox="0 0 20 20"
                >
                  <path
                    fillRule="evenodd"
                    d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z"
                    clipRule="evenodd"
                  />
                </svg>
              </div>
            </div>

            <div className="flex-1 pb-6">
              <p className="text-sm font-bold text-spotiplag-white mb-2 uppercase">
                Artista
              </p>
              <h1 className="text-7xl font-black text-spotiplag-white mb-6 leading-none">
                {artist.name}
              </h1>
              <div className="flex items-center gap-2 text-spotiplag-white">
                <span className="font-semibold">
                  {artist.albums?.length || 0}{" "}
                  {artist.albums?.length === 1 ? "álbum" : "álbuns"}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Content Section */}
      <div className="p-8">
        <h2 className="text-2xl font-bold text-spotiplag-white mb-6">
          Discografia
        </h2>

        {/* Verificação de Álbuns */}
        {artist.albums && artist.albums.length > 0 ? (
          <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-6">
            {artist.albums.map((album) => (
              <div
                key={album.id}
                className="bg-spotiplag-darkgray p-4 rounded-lg hover:bg-spotiplag-gray transition-all group cursor-pointer"
                onClick={() => navigate(`/albums/${album.id}`)}
              >
                <div className="aspect-square mb-4 shadow-lg rounded-md overflow-hidden bg-spotiplag-black">
                  {album.coverUrl ? (
                    <img
                      src={album.coverUrl}
                      alt={album.title}
                      className="w-full h-full object-cover group-hover:scale-105 transition-transform"
                      onError={(e) => {
                        e.currentTarget.style.display = "none";
                        e.currentTarget.parentElement!.innerHTML = `
                          <div class="w-full h-full flex items-center justify-center text-spotiplag-lightwhite text-4xl">
                            💿
                          </div>
                        `;
                      }}
                    />
                  ) : (
                    <div className="w-full h-full flex items-center justify-center text-spotiplag-lightwhite text-4xl">
                      💿
                    </div>
                  )}
                </div>
                <h3 className="text-spotiplag-white font-bold truncate">
                  {album.title}
                </h3>
                <p className="text-spotiplag-lightwhite text-sm">Álbum</p>
              </div>
            ))}
          </div>
        ) : (
          /* Mensagem quando não há álbuns */
          <div className="bg-spotiplag-gray rounded-lg p-12 text-center">
            <div className="w-24 h-24 bg-spotiplag-darkgray rounded-full flex items-center justify-center mx-auto mb-6">
              <svg
                className="w-12 h-12 text-spotiplag-darkwhite"
                fill="currentColor"
                viewBox="0 0 20 20"
              >
                <path d="M18 3a1 1 0 00-1.196-.98l-10 2A1 1 0 006 5v9.114A4.369 4.369 0 005 14c-1.657 0-3 .895-3 2s1.343 2 3 2 3-.895 3-2V7.82l8-1.6v5.894A4.37 4.37 0 0015 12c-1.657 0-3 .895-3 2s1.343 2 3 2 3-.895 3-2V3z" />
              </svg>
            </div>
            <p className="text-spotiplag-lightwhite mb-6 text-lg">
              Este artista ainda não possui álbuns cadastrados.
            </p>
            <Link
              to="/albums/new"
              className="inline-flex items-center gap-2 bg-spotiplag-blue text-spotiplag-black font-bold px-8 py-3 rounded-full hover:scale-105 transition-transform"
            >
              <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                <path
                  fillRule="evenodd"
                  d="M10 3a1 1 0 011 1v5h5a1 1 0 110 2h-5v5a1 1 0 11-2 0v-5H4a1 1 0 110-2h5V4a1 1 0 011-1z"
                  clipRule="evenodd"
                />
              </svg>
              Adicionar Primeiro Álbum
            </Link>
          </div>
        )}
      </div>
    </div>
  );
}
