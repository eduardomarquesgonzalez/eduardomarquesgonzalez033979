import { useEffect, useState } from "react";
import { useParams, Link, useNavigate } from "react-router-dom";
import { useObservable } from "../../../shared/hooks/useObservable";
import albumFacade from "../AlbumFacade";
import LoadingSpinner from "../../../shared/components/LoadingSpinner";

export default function AlbumDetail() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [deletingImageId, setDeletingImageId] = useState<number | null>(null);

  const albumState = useObservable(
    albumFacade.getState(),
    albumFacade.getCurrentState(),
  );

  const { selectedAlbum: album, loading, error } = albumState;

  useEffect(() => {
    if (id) {
      albumFacade.loadAlbumById(Number(id));
    }
    return () => {
      albumFacade.clearSelectedAlbum();
    };
  }, [id]);

  const handleDeleteImage = async (imageId: number) => {
    if (!confirm("Deseja realmente deletar esta imagem?")) return;

    try {
      setDeletingImageId(imageId);
      await albumFacade.deleteImage(Number(id), imageId);
    } catch (err) {
      console.error("Erro ao deletar imagem:", err);
    } finally {
      setDeletingImageId(null);
    }
  };

  const handleDeleteAlbum = async () => {
    if (
      !confirm(
        "Deseja realmente deletar este álbum? Esta ação não pode ser desfeita.",
      )
    )
      return;

    try {
      await albumFacade.deleteAlbum(Number(id));
      navigate("/albums");
    } catch (err) {
      console.error("Erro ao deletar álbum:", err);
    }
  };

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

  if (!album) {
    return (
      <div className="p-8 text-center py-20">
        <svg
          className="w-24 h-24 text-spotiplag-darkwhite mx-auto mb-4"
          fill="currentColor"
          viewBox="0 0 20 20"
        >
          <path d="M18 3a1 1 0 00-1.196-.98l-10 2A1 1 0 006 5v9.114A4.369 4.369 0 005 14c-1.657 0-3 .895-3 2s1.343 2 3 2 3-.895 3-2V7.82l8-1.6v5.894A4.37 4.37 0 0015 12c-1.657 0-3 .895-3 2s1.343 2 3 2 3-.895 3-2V3z" />
        </svg>
        <p className="text-spotiplag-lightwhite text-xl mb-4">
          Álbum não encontrado.
        </p>
        <Link to="/albums" className="btn-primary">
          Voltar para Álbuns
        </Link>
      </div>
    );
  }

  return (
    <div>
      {/* Header Section */}
      <div className="relative bg-gradient-to-b from-spotiplag-blue/20 to-transparent">
        <div className="absolute inset-0 bg-gradient-to-b from-black/40 to-transparent"></div>

        <div className="relative p-8">
          {/* Breadcrumb */}
          <nav className="mb-6 text-sm flex items-center gap-2">
            <Link
              to="/albums"
              className="text-spotiplag-lightwhite hover:text-spotiplag-white transition-colors"
            >
              Álbuns
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
              {album.title}
            </span>
          </nav>

          {/* Album Header */}
          <div className="flex items-end gap-6">
            <div className="flex-shrink-0">
              <div className="w-60 h-60 bg-spotiplag-darkgray rounded-md overflow-hidden shadow-2xl">
                {album.coverUrl ? (
                  <img
                    src={album.coverUrl}
                    alt={album.title}
                    className="w-full h-full object-cover"
                  />
                ) : (
                  <div className="w-full h-full flex items-center justify-center text-spotiplag-lightwhite text-6xl">
                    💿
                  </div>
                )}
              </div>
            </div>

            <div className="flex-1 pb-6">
              <p className="text-sm font-bold text-spotiplag-white mb-2 uppercase">
                Álbum
              </p>
              <h1 className="text-7xl font-black text-spotiplag-white mb-6 leading-none">
                {album.title}
              </h1>
              <div className="flex items-center gap-4 text-spotiplag-white">
                <span className="font-semibold">
                  {album.artists.map((a) => a.name).join(", ")}
                </span>
                <span>•</span>
                <span>
                  {album.images.length}{" "}
                  {album.images.length === 1 ? "imagem" : "imagens"}
                </span>
              </div>

              {/* Action Buttons */}
              <div className="flex gap-3 mt-6">
                <Link
                  to={`/albums/${id}/edit`}
                  className="bg-spotiplag-blue text-spotiplag-black font-bold px-8 py-3 rounded-full hover:scale-105 transition-transform"
                >
                  Editar Álbum
                </Link>
                <button
                  onClick={handleDeleteAlbum}
                  className="bg-red-500 text-white font-bold px-8 py-3 rounded-full hover:bg-red-600 transition-colors"
                >
                  Deletar Álbum
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Images Gallery */}
      <div className="p-8">
        <h2 className="text-2xl font-bold text-spotiplag-white mb-6">
          Galeria de Imagens
        </h2>

        {album.images && album.images.length > 0 ? (
          <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
            {album.images.map((image) => (
              <div
                key={image.id}
                className="relative group bg-spotiplag-darkgray rounded-lg overflow-hidden"
              >
                <div className="aspect-square">
                  <img
                    src={image.url}
                    alt={image.fileName}
                    className="w-full h-full object-cover"
                  />
                </div>

                {/* Image Info Overlay */}
                <div className="absolute inset-0 bg-black/60 opacity-0 group-hover:opacity-100 transition-opacity flex flex-col justify-between p-4">
                  <div>
                    {image.isPrimary && (
                      <span className="inline-block bg-spotiplag-blue text-spotiplag-black text-xs font-bold px-2 py-1 rounded">
                        Capa Principal
                      </span>
                    )}
                  </div>
                  <div>
                    <p className="text-white text-sm font-semibold truncate mb-2">
                      {image.fileName}
                    </p>
                    <button
                      onClick={() => handleDeleteImage(image.id)}
                      disabled={deletingImageId === image.id}
                      className="w-full bg-red-500 text-white font-bold py-2 rounded hover:bg-red-600 transition-colors disabled:opacity-50"
                    >
                      {deletingImageId === image.id
                        ? "Deletando..."
                        : "Deletar"}
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        ) : (
          <div className="bg-spotiplag-gray rounded-lg p-12 text-center">
            <p className="text-spotiplag-lightwhite mb-6 text-lg">
              Este álbum ainda não possui imagens.
            </p>
            <Link
              to={`/albums/${id}/edit`}
              className="inline-flex items-center gap-2 bg-spotiplag-blue text-spotiplag-black font-bold px-8 py-3 rounded-full hover:scale-105 transition-transform"
            >
              Adicionar Imagens
            </Link>
          </div>
        )}
      </div>
    </div>
  );
}
