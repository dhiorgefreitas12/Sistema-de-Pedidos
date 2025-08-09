import { http } from './http';

export type OrderItemDto = {
  orderId: number;
  productId: number;
  quantity: number;
  subtotal?: number;
};

export const listOrderItems = async () =>
  (await http.get<OrderItemDto[]>('/order-items')).data;

export const getOrderItem = async (id: number) =>
  (await http.get<OrderItemDto>(`/order-items/${id}`)).data;

export const deleteOrderItem = async (id: number) =>
  (await http.delete<void>(`/order-items/${id}`)).data;

// use apenas se implementou POST /order-items
export const createOrderItem = async (data: OrderItemDto) =>
  (await http.post<OrderItemDto>('/order-items', data)).data;
