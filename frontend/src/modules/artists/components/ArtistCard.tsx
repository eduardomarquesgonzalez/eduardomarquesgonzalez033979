import { Artist } from "@/shared/models/artist.model";
import { Link } from "react-router-dom";

interface ArtistCardProps {
  artist: Artist;
}

export default function ArtistCard({ artist }: ArtistCardProps) {
  const albumCount = artist.albumCount ?? 0;

  return (
    <div className="group bg-spotiplag-gray rounded-lg p-4 hover:bg-spotiplag-lightgray transition-all duration-300 cursor-pointer">
      <Link to={`/artists/${artist.id}`} className="block">
        <div className="relative mb-4 aspect-square">
          <div className="w-full h-full bg-spotiplag-darkgray rounded-full flex items-center justify-center">
            <svg
              className="w-24 h-24 text-spotiplag-darkwhite"
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

        {/* Artist Info */}
        <div className="mb-3">
          <h3 className="text-spotiplag-white font-bold text-lg truncate mb-1 group-hover:underline">
            {artist.name}
          </h3>
          <p className="text-spotiplag-lightwhite text-sm">
            {albumCount} {albumCount === 1 ? "álbum" : "álbuns"}
          </p>
        </div>
      </Link>

      {/* Action Buttons */}
      <div className="flex gap-2 mt-4">
        <Link
          to={`/artists/${artist.id}/edit`}
          className="flex-1 text-center bg-spotiplag-lightgray text-spotiplag-white px-3 py-2 text-sm rounded-full hover:bg-spotiplag-darkwhite transition-colors"
          onClick={(e) => e.stopPropagation()}
        >
          Editar
        </Link>
      </div>
    </div>
  );
}
