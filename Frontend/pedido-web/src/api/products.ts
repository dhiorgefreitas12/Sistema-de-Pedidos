import { http } from './http';

export type ProductDto = {
  id?: number;
  name: string;
  price: number;
};

export const listProducts = async () =>
  (await http.get<ProductDto[]>('/products')).data;

export const getProduct = async (id: number) =>
  (await http.get<ProductDto>(`/products/${id}`)).data;

export const createProduct = async (data: Omit<ProductDto, 'id'>) =>
  (await http.post<ProductDto>('/products', data)).data;

export const deleteProduct = async (id: number) =>
  (await http.delete<void>(`/products/${id}`)).data;
