export interface Album {
  id: number;
  title: string;
  coverUrl?: string;
  artistNames?: string[];
  imageCount?: number;
}

export interface AlbumDetail {
  id: number;
  title: string;
  coverUrl?: string;
  artists: ArtistSimple[];
  images: AlbumImage[];
}

export interface ArtistSimple {
  id: number;
  name: string;
}

export interface AlbumImage {
  id: number;
  fileName: string;
  url: string;
  contentType: string;
  isPrimary: boolean;
}

export interface CreateAlbumRequest {
  title: string;
  artistIds: number[];
  coverImage?: File;
}

export interface UpdateAlbumRequest {
  title?: string;
  artistIds?: number[];
  coverImage?: File;
}
