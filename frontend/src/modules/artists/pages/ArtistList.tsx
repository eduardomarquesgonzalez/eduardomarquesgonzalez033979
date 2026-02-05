import { useEffect } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { useObservable } from "../../../shared/hooks/useObservable";
import ArtistCard from "../components/ArtistCard";
import ArtistSearchBar from "../components/ArtistSearchBar";
import Pagination from "../../../shared/components/Pagination";
import LoadingSpinner from "../../../shared/components/LoadingSpinner";
import artistFacade from "../../artists/ArtistFacade";
// ... (mantenha os imports iguais)

export default function ArtistList() {
  const [searchParams, setSearchParams] = useSearchParams();

  const artistState = useObservable(
    artistFacade.getState(),
    artistFacade.getCurrentState(),
  );

  const { artists, loading, error } = artistState;

  // Extração de parâmetros da URL
  const searchName = searchParams.get("name") || "";
  const sort = searchParams.get("sort") === "desc" ? "desc" : "asc";
  const currentPage = Number(searchParams.get("page") || 0);
  const pageSize = Number(searchParams.get("size") || 12);

  useEffect(() => {
    artistFacade.loadArtists({
      page: currentPage,
      size: pageSize,
      name: searchName || undefined,
      sort: sort as "asc" | "desc",
    });
    // Omiti o reset no cleanup para evitar flash branco ao navegar
  }, [currentPage, pageSize, searchName, sort]);

  // Handlers (Mantidos iguais, estão ótimos)
  const handleSearch = (name: string) => {
    setSearchParams({ page: "0", size: pageSize.toString(), name, sort });
  };

  const handleSortChange = () => {
    const newSort = sort === "asc" ? "desc" : "asc";
    setSearchParams({ page: currentPage.toString(), size: pageSize.toString(), name: searchName, sort: newSort });
  };

  const handlePageChange = (page: number) => {
    setSearchParams({ page: page.toString(), size: pageSize.toString(), name: searchName, sort });
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  return (
    <div className="p-8">
      {/* Header - Sempre visível */}
      <div className="mb-8 flex justify-between items-end">
        <div>
          <h1 className="text-5xl font-bold text-spotiplag-white mb-4">
            Artistas
          </h1>
          <Link
            to="/artists/new"
            className="inline-flex items-center gap-2 btn-primary"
          >
            + Adicionar Artista
          </Link>
        </div>
      </div>

      {/* Search Bar - Sempre visível */}
      <ArtistSearchBar
        searchValue={searchName}
        onSearch={handleSearch}
        sortDirection={sort}
        onSortChange={handleSortChange}
      />

      {/* Área de Conteúdo com Loading Condicional */}
      {loading ? (
        <div className="flex justify-center py-20">
          <LoadingSpinner />
        </div>
      ) : error ? (
        <div className="bg-red-500/10 border border-red-500 text-red-500 px-4 py-3 rounded-md mb-6">
          {error}
        </div>
      ) : artists && artists.content.length > 0 ? (
        <>
          <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 2xl:grid-cols-6 gap-6 mb-8">
            {artists.content.map((artist) => (
              <ArtistCard key={artist.id} artist={artist} />
            ))}
          </div>

          <Pagination
            currentPage={currentPage}
            totalPages={artists.totalPages}
            onPageChange={handlePageChange}
          />
        </>
      ) : (
        <div className="text-center py-20">
          <p className="text-spotiplag-lightwhite text-xl">
            {searchName
              ? `Nenhum artista encontrado para "${searchName}".`
              : "Nenhum artista cadastrado."}
          </p>
        </div>
      )}
    </div>
  );
}