import { BehaviorSubject, Observable } from "rxjs";
import { artistService, ArtistSearchParams } from "./services/artist.service";
import { Artist } from "@/shared/models/artist.model";
import { ArtistState } from "./stores/artist.store";

class ArtistFacade {
  private state$ = new BehaviorSubject<ArtistState>({
    artists: null,
    selectedArtist: null,
    loading: false,
    error: null,
  });

  getState(): Observable<ArtistState> {
    return this.state$.asObservable();
  }

  getCurrentState(): ArtistState {
    return this.state$.value;
  }

  async loadArtists(params: ArtistSearchParams = {}): Promise<void> {
    try {
      this.setLoading(true);
      const response = await artistService.getAll(params);
      this.state$.next({
        ...this.state$.value,
        artists: response,
        loading: false,
      });
    } catch (error: any) {
      this.setError(
        error.response?.data?.message || "Erro ao carregar artistas",
      );
    }
  }

  async loadArtistById(id: number): Promise<void> {
    try {
      this.setLoading(true);
      const artist = await artistService.getById(id);

      let albums = [];
      try {
        albums = await artistService.getAlbums(id);
      } catch (e) {
        console.warn("Não foi possível carregar os álbuns deste artista.");
      }

      this.state$.next({
        ...this.state$.value,
        selectedArtist: { ...artist, albums },
        loading: false,
      });
    } catch (error: any) {
      this.setError(
        error.response?.data?.message || "Erro ao carregar detalhes do artista",
      );
    }
  }

  async createArtist(data: Partial<Artist>): Promise<Artist> {
    try {
      this.setLoading(true);
      const newArtist = await artistService.create(data);
      this.setLoading(false);
      return newArtist;
    } catch (error: any) {
      this.setError(error.response?.data?.message || "Erro ao criar artista");
      throw error;
    }
  }

  async updateArtist(id: number, data: Partial<Artist>): Promise<Artist> {
    try {
      this.setLoading(true);
      const updated = await artistService.update(id, data);
      this.setLoading(false);
      return updated;
    } catch (error: any) {
      this.setError(
        error.response?.data?.message || "Erro ao atualizar artista",
      );
      throw error;
    }
  }

  clearSelectedArtist() {
    this.state$.next({
      ...this.state$.value,
      selectedArtist: null,
      error: null,
    });
  }

  private setLoading(loading: boolean) {
    this.state$.next({ ...this.state$.value, loading, error: null });
  }

  private setError(message: string) {
    this.state$.next({ ...this.state$.value, loading: false, error: message });
  }

  reset() {
    this.state$.next({
      artists: null,
      selectedArtist: null,
      loading: false,
      error: null,
    });
  }
}

export default new ArtistFacade();
