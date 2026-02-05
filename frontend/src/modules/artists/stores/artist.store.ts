import { Artist } from '@/shared/models/artist.model'
import { PaginatedResponse } from '@/shared/models/pagination.model'
import { BehaviorSubject, Observable } from 'rxjs'


export interface ArtistState {
  artists: PaginatedResponse<Artist> | null
  selectedArtist: Artist | null
  loading: boolean
  error: string | null
}

class ArtistStore {
  private state = new BehaviorSubject<ArtistState>({
    artists: null,
    selectedArtist: null,
    loading: false,
    error: null,
  })

  getState(): Observable<ArtistState> {
    return this.state.asObservable()
  }

  getCurrentState(): ArtistState {
    return this.state.value
  }

  setLoading(loading: boolean) {
    this.state.next({ ...this.state.value, loading, error: null })
  }

  setArtists(artists: PaginatedResponse<Artist>) {
    this.state.next({
      ...this.state.value,
      artists,
      loading: false,
      error: null,
    })
  }

  setSelectedArtist(artist: Artist | null) {
    this.state.next({
      ...this.state.value,
      selectedArtist: artist,
      loading: false,
      error: null,
    })
  }

  setError(error: string) {
    this.state.next({
      ...this.state.value,
      loading: false,
      error,
    })
  }

  addArtist(artist: Artist) {
    const currentState = this.state.value
    if (currentState.artists) {
      const updatedContent = [artist, ...currentState.artists.content]
      this.state.next({
        ...currentState,
        artists: {
          ...currentState.artists,
          content: updatedContent,
          totalElements: currentState.artists.totalElements + 1,
        },
      })
    }
  }

  updateArtist(artist: Artist) {
    const currentState = this.state.value
    if (currentState.artists) {
      const updatedContent = currentState.artists.content.map((a) =>
        a.id === artist.id ? artist : a
      )
      this.state.next({
        ...currentState,
        artists: {
          ...currentState.artists,
          content: updatedContent,
        },
      })
    }
  }

  removeArtist(id: number) {
    const currentState = this.state.value
    if (currentState.artists) {
      const updatedContent = currentState.artists.content.filter((a) => a.id !== id)
      this.state.next({
        ...currentState,
        artists: {
          ...currentState.artists,
          content: updatedContent,
          totalElements: currentState.artists.totalElements - 1,
        },
      })
    }
  }
}

export const artistStore = new ArtistStore()
