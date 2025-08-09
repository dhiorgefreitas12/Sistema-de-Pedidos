import { http } from './http';

export type OrderItemDto = {
  productId: number;
  quantity: number;
  subtotal?: number;
};

export type OrderDto = {
  id?: number;
  clientId: number;
  orderDate: string;
  total: number;
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
  items: OrderItemDto[];
};

export const listOrders = async () =>
  (await http.get<OrderDto[]>('/orders')).data;

export const getOrder = async (id: number) =>
  (await http.get<OrderDto>(`/orders/${id}`)).data;

export const createOrder = async (data: OrderDto) =>
  (await http.post<OrderDto>('/orders', data)).data;

export const deleteOrder = async (id: number) =>
  (await http.delete<void>(`/orders/${id}`)).data;
