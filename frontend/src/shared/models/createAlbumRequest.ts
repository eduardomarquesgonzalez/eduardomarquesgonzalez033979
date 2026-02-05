export interface CreateAlbumRequest {
  title: string
  coverImage?: File
  artistIds: number[]
}