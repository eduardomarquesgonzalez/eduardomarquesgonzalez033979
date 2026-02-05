interface PaginationProps {
  currentPage: number;
  totalPages: number;
  onPageChange: (page: number) => void;
}

export default function Pagination({
  currentPage,
  totalPages,
  onPageChange,
}: PaginationProps) {
  const pages = [];
  const maxVisible = 5;

  let startPage = Math.max(0, currentPage - Math.floor(maxVisible / 2));
  let endPage = Math.min(totalPages - 1, startPage + maxVisible - 1);

  if (endPage - startPage < maxVisible - 1) {
    startPage = Math.max(0, endPage - maxVisible + 1);
  }

  for (let i = startPage; i <= endPage; i++) {
    pages.push(i);
  }

  if (totalPages <= 1) return null;

  return (
    <div className="flex justify-center items-center gap-2 mt-8">
      <button
        onClick={() => onPageChange(currentPage - 1)}
        disabled={currentPage === 0}
        className="w-10 h-10 rounded-full bg-spotiplag-lightgray hover:bg-spotiplag-darkwhite disabled:opacity-30 disabled:cursor-not-allowed transition-colors flex items-center justify-center"
      >
        <svg
          className="w-5 h-5 text-spotiplag-white"
          fill="currentColor"
          viewBox="0 0 20 20"
        >
          <path
            fillRule="evenodd"
            d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z"
            clipRule="evenodd"
          />
        </svg>
      </button>

      {startPage > 0 && (
        <>
          <button
            onClick={() => onPageChange(0)}
            className="w-10 h-10 rounded-full bg-spotiplag-lightgray hover:bg-spotiplag-darkwhite transition-colors text-spotiplag-white font-semibold"
          >
            1
          </button>
          {startPage > 1 && (
            <span className="text-spotiplag-lightwhite">...</span>
          )}
        </>
      )}

      {pages.map((page) => (
        <button
          key={page}
          onClick={() => onPageChange(page)}
          className={`w-10 h-10 rounded-full font-semibold transition-all ${
            page === currentPage
              ? "bg-spotiplag-blue text-spotiplag-black scale-110"
              : "bg-spotiplag-lightgray hover:bg-spotiplag-darkwhite text-spotiplag-white"
          }`}
        >
          {page + 1}
        </button>
      ))}

      {endPage < totalPages - 1 && (
        <>
          {endPage < totalPages - 2 && (
            <span className="text-spotiplag-lightwhite">...</span>
          )}
          <button
            onClick={() => onPageChange(totalPages - 1)}
            className="w-10 h-10 rounded-full bg-spotiplag-lightgray hover:bg-spotiplag-darkwhite transition-colors text-spotiplag-white font-semibold"
          >
            {totalPages}
          </button>
        </>
      )}

      <button
        onClick={() => onPageChange(currentPage + 1)}
        disabled={currentPage >= totalPages - 1}
        className="w-10 h-10 rounded-full bg-spotiplag-lightgray hover:bg-spotiplag-darkwhite disabled:opacity-30 disabled:cursor-not-allowed transition-colors flex items-center justify-center"
      >
        <svg
          className="w-5 h-5 text-spotiplag-white"
          fill="currentColor"
          viewBox="0 0 20 20"
        >
          <path
            fillRule="evenodd"
            d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z"
            clipRule="evenodd"
          />
        </svg>
      </button>
    </div>
  );
}
