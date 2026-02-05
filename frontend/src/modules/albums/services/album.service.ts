import { httpService } from "../../../shared/services/http";
import {
  Album,
  AlbumDetail,
  CreateAlbumRequest,
  UpdateAlbumRequest,
} from "../../../shared/models/album.model";
import {
  PaginatedResponse,
  PaginationParams,
} from "../../../shared/models/pagination.model";

export interface AlbumSearchParams extends PaginationParams {
  title?: string;
  artistId?: number;
}

class AlbumService {
  private baseUrl = "/v1/albums";

  async getAll(params?: AlbumSearchParams): Promise<PaginatedResponse<Album>> {
    return httpService.get<PaginatedResponse<Album>>(this.baseUrl, { params });
  }

  async getById(id: number): Promise<AlbumDetail> {
    return httpService.get<AlbumDetail>(`${this.baseUrl}/${id}`);
  }

  async create(request: CreateAlbumRequest): Promise<Album> {
    const formData = new FormData();
    formData.append("title", request.title);

    request.artistIds.forEach((id) => {
      formData.append("artistIds", id.toString());
    });

    if (request.coverImage) {
      formData.append("images", request.coverImage);
    }

    return httpService.post<Album>(this.baseUrl, formData, {
      headers: { "Content-Type": "multipart/form-data" },
    });
  }

  async update(id: number, request: UpdateAlbumRequest): Promise<Album> {
    const formData = new FormData();

    if (request.title) {
      formData.append("title", request.title);
    }

    if (request.artistIds) {
      request.artistIds.forEach((artistId) => {
        formData.append("artistIds", artistId.toString());
      });
    }

    if (request.coverImage) {
      formData.append("images", request.coverImage);
    }

    return httpService.put<Album>(`${this.baseUrl}/${id}`, formData, {
      headers: { "Content-Type": "multipart/form-data" },
    });
  }

  async delete(id: number): Promise<void> {
    return httpService.delete(`${this.baseUrl}/${id}`);
  }

  async deleteImage(albumId: number, imageId: number): Promise<void> {
    return httpService.delete(`${this.baseUrl}/${albumId}/images/${imageId}`);
  }
}

export const albumService = new AlbumService();
