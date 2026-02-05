import { httpService } from "../../../shared/services/http";
import { Artist } from "../../../shared/models/artist.model";
import {
  PaginatedResponse,
  PaginationParams,
} from "../../../shared/models/pagination.model";

export interface ArtistSearchParams extends PaginationParams {
  name?: string;
}

class ArtistService {
  private baseUrl = "/v1/artists";

  async getAll(
    params?: ArtistSearchParams,
  ): Promise<PaginatedResponse<Artist>> {
    return httpService.get<PaginatedResponse<Artist>>(this.baseUrl, { params });
  }

  async getById(id: number): Promise<Artist> {
    return httpService.get<Artist>(`${this.baseUrl}/${id}`);
  }

  async create(artist: Partial<Artist>): Promise<Artist> {
    return httpService.post<Artist>(this.baseUrl, { name: artist.name });
  }

  async update(id: number, artist: Partial<Artist>): Promise<Artist> {
    return httpService.put<Artist>(`${this.baseUrl}/${id}`, {
      name: artist.name,
    });
  }

  async getAlbums(id: number): Promise<any[]> {
    return httpService.get<any[]>(`${this.baseUrl}/${id}/albums`);
  }

  async uploadPhoto(id: number, file: File): Promise<string> {
    const formData = new FormData();
    formData.append("file", file);
    return httpService.post<string>(`${this.baseUrl}/${id}/photo`, formData, {
      headers: { "Content-Type": "multipart/form-data" },
    });
  }

  async createWithPhoto(
    artist: Partial<Artist>,
    photo?: File,
  ): Promise<Artist> {
    const createdArtist = await this.create(artist);

    if (photo && createdArtist.id) {
      const photoUrl = await this.uploadPhoto(createdArtist.id, photo);
      return { ...createdArtist, photoUrl };
    }

    return createdArtist;
  }

  async updateWithPhoto(
    id: number,
    artist: Partial<Artist>,
    photo?: File,
  ): Promise<Artist> {
    const updatedArtist = await this.update(id, artist);

    if (photo) {
      const photoUrl = await this.uploadPhoto(id, photo);
      return { ...updatedArtist, photoUrl };
    }

    return updatedArtist;
  }

}

export const artistService = new ArtistService();
