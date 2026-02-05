import { Album } from "@/shared/models/album.model";
import { Link } from "react-router-dom";

interface AlbumCardProps {
  album: Album;
}

export default function AlbumCard({ album }: AlbumCardProps) {
  const imageCount = album.imageCount ?? 0;

  return (
    <div className="group bg-spotiplag-gray rounded-lg p-4 hover:bg-spotiplag-lightgray transition-all duration-300 cursor-pointer">
      <Link to={`/albums/${album.id}`} className="block">
        {/* Album Cover */}
        <div className="relative mb-4 aspect-square">
          <div className="w-full h-full bg-spotiplag-darkgray rounded-md overflow-hidden">
            {album.coverUrl ? (
              <img
                src={album.coverUrl}
                alt={album.title}
                className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
                onError={(e) => {
                  e.currentTarget.style.display = "none";
                  const parent = e.currentTarget.parentElement;
                  if (parent) {
                    parent.innerHTML = `
                      <div class="w-full h-full flex items-center justify-center text-spotiplag-lightwhite text-6xl">
                        💿
                      </div>
                    `;
                  }
                }}
              />
            ) : (
              <div className="w-full h-full flex items-center justify-center text-spotiplag-lightwhite text-6xl">
                💿
              </div>
            )}
          </div>
        </div>

        {/* Album Info */}
        <div className="mb-3">
          <h3 className="text-spotiplag-white font-bold text-lg truncate mb-1 group-hover:underline">
            {album.title}
          </h3>
          <p className="text-spotiplag-lightwhite text-sm truncate">
            {album.artistNames && album.artistNames.length > 0
              ? album.artistNames.join(", ")
              : "Artista desconhecido"}
          </p>
          <p className="text-spotiplag-lightwhite text-xs mt-1">
            {imageCount} {imageCount === 1 ? "imagem" : "imagens"}
          </p>
        </div>
      </Link>

      {/* Action Buttons */}
      <div className="flex gap-2 mt-4">
        <Link
          to={`/albums/${album.id}/edit`}
          className="flex-1 text-center bg-spotiplag-lightgray text-spotiplag-white px-3 py-2 text-sm rounded-full hover:bg-spotiplag-darkwhite transition-colors"
          onClick={(e) => e.stopPropagation()}
        >
          Editar
        </Link>
      </div>
    </div>
  );
}
