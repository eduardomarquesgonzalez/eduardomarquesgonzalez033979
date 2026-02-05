import { Album } from "./album.model"


export interface Artist {
  id: number
  name: string
  photoUrl?: string
  albumCount?: number
  albums?: Album[]
}
