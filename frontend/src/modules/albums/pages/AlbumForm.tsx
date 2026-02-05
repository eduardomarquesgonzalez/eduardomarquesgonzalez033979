import { useState, useEffect } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";
import { useObservable } from "../../../shared/hooks/useObservable";
import albumFacade from "../AlbumFacade";
import { artistService } from "../../artists/services/artist.service";
import AlbumImageUpload from "../components/AlbumImageUpload";
import LoadingSpinner from "../../../shared/components/LoadingSpinner";
import { Artist } from "@/shared/models/artist.model";

export default function AlbumForm() {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const isEditing = !!id;

  const {
    selectedAlbum,
    loading,
    error: apiError,
  } = useObservable(albumFacade.getState(), albumFacade.getCurrentState());

  // Form State
  const [title, setTitle] = useState("");
  const [selectedArtistIds, setSelectedArtistIds] = useState<number[]>([]);
  const [coverImage, setCoverImage] = useState<File | null>(null);
  const [currentCoverUrl, setCurrentCoverUrl] = useState<string | undefined>();

  // Available Artists
  const [availableArtists, setAvailableArtists] = useState<Artist[]>([]);
  const [loadingArtists, setLoadingArtists] = useState(false);

  // UI State
  const [submitting, setSubmitting] = useState(false);
  const [localError, setLocalError] = useState<string | null>(null);

  // Load album data if editing
  useEffect(() => {
    if (isEditing && id) {
      albumFacade.loadAlbumById(Number(id));
    }
    return () => {
      albumFacade.clearSelectedAlbum();
    };
  }, [id, isEditing]);

  // Populate form when album is loaded
  useEffect(() => {
    if (isEditing && selectedAlbum) {
      setTitle(selectedAlbum.title);
      setSelectedArtistIds(selectedAlbum.artists.map((a) => a.id));
      setCurrentCoverUrl(selectedAlbum.coverUrl);
    }
  }, [selectedAlbum, isEditing]);

  // Load available artists
  useEffect(() => {
    loadArtists();
  }, []);

  const loadArtists = async () => {
    try {
      setLoadingArtists(true);
      const response = await artistService.getAll({ page: 0, size: 100 });
      setAvailableArtists(response.content);
    } catch (err) {
      console.error("Erro ao carregar artistas:", err);
      setLocalError("Erro ao carregar lista de artistas");
    } finally {
      setLoadingArtists(false);
    }
  };

  const toggleArtist = (artistId: number) => {
    setSelectedArtistIds((prev) =>
      prev.includes(artistId)
        ? prev.filter((id) => id !== artistId)
        : [...prev, artistId],
    );
  };

  const handleImageSelect = (file: File) => {
    setCoverImage(file);
    setLocalError(null);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    // Validações
    if (!title.trim()) {
      setLocalError("O título do álbum é obrigatório.");
      return;
    }

    if (selectedArtistIds.length === 0) {
      setLocalError("Selecione pelo menos um artista.");
      return;
    }

    setSubmitting(true);
    setLocalError(null);

    try {
      if (isEditing && id) {
        // Update existing album
        await albumFacade.updateAlbum(Number(id), {
          title,
          artistIds: selectedArtistIds,
          coverImage: coverImage || undefined,
        });
        navigate(`/albums/${id}`);
      } else {
        // Create new album
        const created = await albumFacade.createAlbum({
          title,
          artistIds: selectedArtistIds,
          coverImage: coverImage || undefined,
        });
        navigate(`/albums/${created.id}`);
      }
    } catch (err: any) {
      console.error("Erro ao salvar:", err);
      setLocalError(err.message || "Erro ao salvar álbum");
    } finally {
      setSubmitting(false);
    }
  };

  if (loading && isEditing) return <LoadingSpinner />;

  return (
    <div className="p-8 max-w-4xl mx-auto">
      {/* Breadcrumb */}
      <nav className="mb-6 text-sm flex items-center gap-2 text-spotiplag-lightwhite">
        <Link to="/albums" className="hover:text-spotiplag-white">
          Álbuns
        </Link>
        <span>/</span>
        <span className="text-spotiplag-white font-semibold">
          {isEditing ? "Editar Álbum" : "Novo Álbum"}
        </span>
      </nav>

      <h1 className="text-4xl font-bold text-spotiplag-white mb-8">
        {isEditing
          ? `Editar: ${selectedAlbum?.title || ""}`
          : "Adicionar Novo Álbum"}
      </h1>

      {(apiError || localError) && (
        <div className="bg-red-500/10 border border-red-500 text-red-500 px-4 py-3 rounded-md mb-6">
          {apiError || localError}
        </div>
      )}

      <form
        onSubmit={handleSubmit}
        className="bg-spotiplag-gray p-8 rounded-lg shadow-xl space-y-8"
      >
        {/* Title Input */}
        <div>
          <label
            htmlFor="title"
            className="block text-sm font-bold text-spotiplag-white mb-2"
          >
            Título do Álbum *
          </label>
          <input
            type="text"
            id="title"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            className="w-full bg-spotiplag-darkgray border border-transparent focus:border-spotiplag-blue p-3 rounded text-white outline-none transition-all"
            placeholder="Ex: Abbey Road"
            required
          />
        </div>

        {/* Album Cover Upload */}
        <AlbumImageUpload
          onImageSelect={handleImageSelect}
          currentImageUrl={currentCoverUrl}
        />

        {/* Artist Selection */}
        <div>
          <label className="block text-sm font-bold text-spotiplag-white mb-2">
            Artistas *
          </label>

          {loadingArtists ? (
            <div className="flex justify-center py-8">
              <LoadingSpinner />
            </div>
          ) : availableArtists.length > 0 ? (
            <>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-3 max-h-80 overflow-y-auto p-4 bg-spotiplag-darkgray rounded-lg">
                {availableArtists.map((artist) => (
                  <label
                    key={artist.id}
                    className="flex items-center gap-3 cursor-pointer hover:bg-spotiplag-gray p-3 rounded transition-colors"
                  >
                    <input
                      type="checkbox"
                      checked={selectedArtistIds.includes(artist.id)}
                      onChange={() => toggleArtist(artist.id)}
                      className="w-5 h-5 text-spotiplag-blue focus:ring-spotiplag-blue rounded"
                    />
                    <div className="flex items-center gap-3 flex-1">
                      <div className="w-10 h-10 bg-spotiplag-lightgray rounded-full flex items-center justify-center flex-shrink-0">
                        <svg
                          className="w-6 h-6 text-spotiplag-darkwhite"
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
                      <span className="text-spotiplag-white font-medium">
                        {artist.name}
                      </span>
                    </div>
                  </label>
                ))}
              </div>

              <p className="text-spotiplag-lightwhite text-sm mt-3">
                {selectedArtistIds.length} artista(s) selecionado(s)
              </p>
            </>
          ) : (
            <div className="bg-spotiplag-darkgray p-8 rounded-lg text-center">
              <p className="text-spotiplag-lightwhite mb-4">
                Nenhum artista cadastrado.
              </p>
              <Link
                to="/artists/new"
                className="inline-flex items-center gap-2 btn-primary"
                target="_blank"
              >
                + Cadastrar Artista
              </Link>
            </div>
          )}
        </div>

        {/* Form Actions */}
        <div className="flex gap-4 pt-4">
          <button
            type="submit"
            disabled={submitting || loadingArtists}
            className="flex-1 btn-primary disabled:opacity-50 font-bold py-3"
          >
            {submitting
              ? "Salvando..."
              : isEditing
                ? "Salvar Alterações"
                : "Criar Álbum"}
          </button>
          <button
            type="button"
            onClick={() => navigate(-1)}
            className="flex-1 px-6 py-3 border border-spotiplag-lightwhite text-spotiplag-white rounded-full hover:border-white font-bold transition-all"
          >
            Cancelar
          </button>
        </div>

        {/* Additional Info */}
        {isEditing && selectedAlbum && (
          <div className="pt-6 border-t border-spotiplag-lightgray">
            <h3 className="text-sm font-bold text-spotiplag-white mb-3">
              Informações do Álbum
            </h3>
            <div className="space-y-2 text-sm text-spotiplag-lightwhite">
              <p>
                <span className="font-semibold">ID:</span> {selectedAlbum.id}
              </p>
              <p>
                <span className="font-semibold">Imagens:</span>{" "}
                {selectedAlbum.images.length}
              </p>
              <p>
                <span className="font-semibold">Artistas atuais:</span>{" "}
                {selectedAlbum.artists.map((a) => a.name).join(", ")}
              </p>
            </div>
          </div>
        )}
      </form>

      {/* Help Text */}
      <div className="mt-6 p-4 bg-spotiplag-darkgray rounded-lg">
        <h4 className="text-spotiplag-white font-semibold mb-2 flex items-center gap-2">
          <svg
            className="w-5 h-5 text-spotiplag-blue"
            fill="currentColor"
            viewBox="0 0 20 20"
          >
            <path
              fillRule="evenodd"
              d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z"
              clipRule="evenodd"
            />
          </svg>
          Dicas
        </h4>
        <ul className="text-spotiplag-lightwhite text-sm space-y-1 ml-7">
          <li>• A imagem da capa deve ter no máximo 5MB</li>
          <li>• Formatos aceitos: JPG, PNG, GIF, WEBP</li>
          <li>• Você pode adicionar mais imagens após criar o álbum</li>
          <li>
            • Selecione pelo menos um artista (use Ctrl/Cmd para seleção
            múltipla)
          </li>
        </ul>
      </div>
    </div>
  );
}
