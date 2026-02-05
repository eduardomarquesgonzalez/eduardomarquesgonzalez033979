import { useEffect } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { useObservable } from "../../../shared/hooks/useObservable";
import AlbumCard from "../components/AlbumCard";
import AlbumSearchBar from "../components/AlbumSearchBar";
import Pagination from "../../../shared/components/Pagination";
import LoadingSpinner from "../../../shared/components/LoadingSpinner";
import albumFacade from "../AlbumFacade";

export default function AlbumList() {
  const [searchParams, setSearchParams] = useSearchParams();

  const albumState = useObservable(
    albumFacade.getState(),
    albumFacade.getCurrentState(),
  );

  const { albums, loading, error } = albumState;

  const searchTitle = searchParams.get("title") || "";
  const sort = searchParams.get("sort") === "desc" ? "desc" : "asc";
  const currentPage = Number(searchParams.get("page") || 0);
  const pageSize = Number(searchParams.get("size") || 12);

  useEffect(() => {
    albumFacade.loadAlbums({
      page: currentPage,
      size: pageSize,
      title: searchTitle || undefined,
      sort: sort as "asc" | "desc",
    });
  }, [currentPage, pageSize, searchTitle, sort]);

  const handleSearch = (title: string) => {
    setSearchParams({ page: "0", size: pageSize.toString(), title, sort });
  };

  const handleSortChange = () => {
    const newSort = sort === "asc" ? "desc" : "asc";
    setSearchParams({
      page: currentPage.toString(),
      size: pageSize.toString(),
      title: searchTitle,
      sort: newSort,
    });
  };

  const handlePageChange = (page: number) => {
    setSearchParams({
      page: page.toString(),
      size: pageSize.toString(),
      title: searchTitle,
      sort,
    });
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  return (
    <div className="p-8">
      {/* Header */}
      <div className="mb-8 flex justify-between items-end">
        <div>
          <h1 className="text-5xl font-bold text-spotiplag-white mb-4">Álbuns</h1>
          <Link
            to="/albums/new"
            className="inline-flex items-center gap-2 btn-primary"
          >
            + Adicionar Álbum
          </Link>
        </div>
      </div>

      {/* Search Bar */}
      <AlbumSearchBar
        searchValue={searchTitle}
        onSearch={handleSearch}
        sortDirection={sort}
        onSortChange={handleSortChange}
      />

      {/* Content Area */}
      {loading ? (
        <div className="flex justify-center py-20">
          <LoadingSpinner />
        </div>
      ) : error ? (
        <div className="bg-red-500/10 border border-red-500 text-red-500 px-4 py-3 rounded-md mb-6">
          {error}
        </div>
      ) : albums && albums.content.length > 0 ? (
        <>
          <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 2xl:grid-cols-6 gap-6 mb-8">
            {albums.content.map((album) => (
              <AlbumCard key={album.id} album={album} />
            ))}
          </div>

          <Pagination
            currentPage={currentPage}
            totalPages={albums.totalPages}
            onPageChange={handlePageChange}
          />
        </>
      ) : (
        <div className="text-center py-20">
          <p className="text-spotiplag-lightwhite text-xl">
            {searchTitle
              ? `Nenhum álbum encontrado para "${searchTitle}".`
              : "Nenhum álbum cadastrado."}
          </p>
        </div>
      )}
    </div>
  );
}