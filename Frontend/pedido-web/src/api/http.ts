import axios from 'axios';

export const http = axios.create({
  baseURL: import.meta.env.VITE_API_URL ?? 'http://localhost:8080',
});

http.interceptors.response.use(
  (r) => r,
  (err) => {
    const msg =
      err?.response?.data ??
      err?.message ??
      'Erro de comunicação com o servidor';
    return Promise.reject(new Error(msg));
  },
);
