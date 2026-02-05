import { BehaviorSubject, Observable } from "rxjs";
import { albumService, AlbumSearchParams } from "./services/album.service";
import {
  Album,
  AlbumDetail,
  CreateAlbumRequest,
  UpdateAlbumRequest,
} from "@/shared/models/album.model";
import { AlbumState } from "./stores/album.store";

class AlbumFacade {
  private state$ = new BehaviorSubject<AlbumState>({
    albums: null,
    selectedAlbum: null,
    loading: false,
    error: null,
  });

  getState(): Observable<AlbumState> {
    return this.state$.asObservable();
  }

  getCurrentState(): AlbumState {
    return this.state$.value;
  }

  async loadAlbums(params: AlbumSearchParams = {}): Promise<void> {
    try {
      this.setLoading(true);
      const response = await albumService.getAll(params);
      this.state$.next({
        ...this.state$.value,
        albums: response,
        loading: false,
      });
    } catch (error: any) {
      this.setError(error.response?.data?.message || "Erro ao carregar álbuns");
    }
  }

  async loadAlbumById(id: number): Promise<void> {
    try {
      this.setLoading(true);
      const album = await albumService.getById(id);
      this.state$.next({
        ...this.state$.value,
        selectedAlbum: album,
        loading: false,
      });
    } catch (error: any) {
      this.setError(
        error.response?.data?.message || "Erro ao carregar detalhes do álbum",
      );
    }
  }

  async createAlbum(data: CreateAlbumRequest): Promise<Album> {
    try {
      this.setLoading(true);
      const newAlbum = await albumService.create(data);
      this.setLoading(false);
      return newAlbum;
    } catch (error: any) {
      this.setError(error.response?.data?.message || "Erro ao criar álbum");
      throw error;
    }
  }

  async updateAlbum(id: number, data: UpdateAlbumRequest): Promise<Album> {
    try {
      this.setLoading(true);
      const updated = await albumService.update(id, data);
      this.setLoading(false);
      return updated;
    } catch (error: any) {
      this.setError(error.response?.data?.message || "Erro ao atualizar álbum");
      throw error;
    }
  }

  async deleteAlbum(id: number): Promise<void> {
    try {
      this.setLoading(true);
      await albumService.delete(id);
      this.setLoading(false);
    } catch (error: any) {
      this.setError(error.response?.data?.message || "Erro ao deletar álbum");
      throw error;
    }
  }

  async deleteImage(albumId: number, imageId: number): Promise<void> {
    try {
      this.setLoading(true);
      await albumService.deleteImage(albumId, imageId);

      // Recarrega o álbum após deletar imagem
      await this.loadAlbumById(albumId);
    } catch (error: any) {
      this.setError(error.response?.data?.message || "Erro ao deletar imagem");
      throw error;
    }
  }

  clearSelectedAlbum() {
    this.state$.next({
      ...this.state$.value,
      selectedAlbum: null,
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
      albums: null,
      selectedAlbum: null,
      loading: false,
      error: null,
    });
  }
}

export default new AlbumFacade();
