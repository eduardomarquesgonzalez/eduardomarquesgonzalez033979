import { Album, AlbumDetail } from "@/shared/models/album.model";
import { PaginatedResponse } from "@/shared/models/pagination.model";
import { BehaviorSubject, Observable } from "rxjs";

export interface AlbumState {
  albums: PaginatedResponse<Album> | null;
  selectedAlbum: AlbumDetail | null;
  loading: boolean;
  error: string | null;
}

class AlbumStore {
  private state = new BehaviorSubject<AlbumState>({
    albums: null,
    selectedAlbum: null,
    loading: false,
    error: null,
  });

  getState(): Observable<AlbumState> {
    return this.state.asObservable();
  }

  getCurrentState(): AlbumState {
    return this.state.value;
  }

  setLoading(loading: boolean) {
    this.state.next({ ...this.state.value, loading, error: null });
  }

  setAlbums(albums: PaginatedResponse<Album>) {
    this.state.next({
      ...this.state.value,
      albums,
      loading: false,
      error: null,
    });
  }

  setSelectedAlbum(album: AlbumDetail | null) {
    this.state.next({
      ...this.state.value,
      selectedAlbum: album,
      loading: false,
      error: null,
    });
  }

  setError(error: string) {
    this.state.next({
      ...this.state.value,
      loading: false,
      error,
    });
  }

  addAlbum(album: Album) {
    const currentState = this.state.value;
    if (currentState.albums) {
      const updatedContent = [album, ...currentState.albums.content];
      this.state.next({
        ...currentState,
        albums: {
          ...currentState.albums,
          content: updatedContent,
          totalElements: currentState.albums.totalElements + 1,
        },
      });
    }
  }

  updateAlbum(album: Album) {
    const currentState = this.state.value;
    if (currentState.albums) {
      const updatedContent = currentState.albums.content.map((a) =>
        a.id === album.id ? album : a,
      );
      this.state.next({
        ...currentState,
        albums: {
          ...currentState.albums,
          content: updatedContent,
        },
      });
    }
  }

  removeAlbum(id: number) {
    const currentState = this.state.value;
    if (currentState.albums) {
      const updatedContent = currentState.albums.content.filter(
        (a) => a.id !== id,
      );
      this.state.next({
        ...currentState,
        albums: {
          ...currentState.albums,
          content: updatedContent,
          totalElements: currentState.albums.totalElements - 1,
        },
      });
    }
  }
}

export const albumStore = new AlbumStore();
