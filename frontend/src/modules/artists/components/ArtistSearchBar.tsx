import { useState } from "react";

interface ArtistSearchBarProps {
  searchValue: string;
  onSearch: (value: string) => void;
  sortDirection: "asc" | "desc";
  onSortChange: () => void;
}

export default function ArtistSearchBar({
  searchValue,
  onSearch,
  sortDirection,
  onSortChange,
}: ArtistSearchBarProps) {
  const [inputValue, setInputValue] = useState(searchValue);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSearch(inputValue);
  };

  return (
    <div className="mb-6">
      <form onSubmit={handleSubmit} className="flex gap-3 items-center">
        {/* Search Input */}
        <div className="flex-1 relative">
          <div className="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none">
            <svg
              className="w-5 h-5 text-spotiplag-lightwhite"
              fill="currentColor"
              viewBox="0 0 20 20"
            >
              <path
                fillRule="evenodd"
                d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z"
                clipRule="evenodd"
              />
            </svg>
          </div>
          <input
            type="text"
            value={inputValue}
            onChange={(e) => setInputValue(e.target.value)}
            placeholder="Buscar artistas..."
            className="w-full pl-12 pr-4 py-3 bg-spotiplag-lightgray border-2 border-transparent text-spotiplag-white placeholder-spotiplag-lightwhite rounded-full focus:border-spotiplag-white focus:outline-none transition-all"
          />
        </div>

        {/* Sort Button */}
        <button
          type="button"
          onClick={onSortChange}
          className="bg-spotiplag-lightgray text-spotiplag-white px-6 py-3 rounded-full hover:bg-spotiplag-darkwhite transition-colors flex items-center gap-2 font-semibold"
          title={sortDirection === "asc" ? "Ordenar Z-A" : "Ordenar A-Z"}
        >
          <span>A-Z</span>
          {sortDirection === "asc" ? (
            <svg className="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
              <path
                fillRule="evenodd"
                d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
                clipRule="evenodd"
              />
            </svg>
          ) : (
            <svg className="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
              <path
                fillRule="evenodd"
                d="M14.707 12.707a1 1 0 01-1.414 0L10 9.414l-3.293 3.293a1 1 0 01-1.414-1.414l4-4a1 1 0 011.414 0l4 4a1 1 0 010 1.414z"
                clipRule="evenodd"
              />
            </svg>
          )}
        </button>

        {/* Clear Button */}
        {inputValue && (
          <button
            type="button"
            onClick={() => {
              setInputValue("");
              onSearch("");
            }}
            className="text-spotiplag-lightwhite hover:text-spotiplag-white transition-colors px-4 py-3 rounded-full hover:bg-spotiplag-lightgray"
            title="Limpar busca"
          >
            <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
              <path
                fillRule="evenodd"
                d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                clipRule="evenodd"
              />
            </svg>
          </button>
        )}
      </form>
    </div>
  );
}
