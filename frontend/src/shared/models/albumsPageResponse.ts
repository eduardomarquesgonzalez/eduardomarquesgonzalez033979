import { Album } from "./album.model";

export interface AlbumsPageResponse {
  content: Album[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
