export interface PaginatedResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}

export interface PaginationParams {
  page?: number;
  size?: number;
  sort?: "asc" | "desc";
}
