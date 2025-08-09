import { http } from './http';

export type ClientDto = {
  id?: number;
  name: string;
  creditLimit: number;
};

export const listClients = async () =>
  (await http.get<ClientDto[]>('/clients')).data;

export const getClient = async (id: number) =>
  (await http.get<ClientDto>(`/clients/${id}`)).data;

export const createClient = async (data: Omit<ClientDto, 'id'>) =>
  (await http.post<ClientDto>('/clients', data)).data;

export const deleteClient = async (id: number) =>
  (await http.delete<void>(`/clients/${id}`)).data;
