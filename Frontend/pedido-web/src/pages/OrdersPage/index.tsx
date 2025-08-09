import { useEffect, useState } from 'react';
import { http } from '../../api/http';
import {
  Typography,
  Box,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  Chip,
} from '@mui/material';

type OrderItemDto = { productId: number; quantity: number; subtotal?: number };
type OrderDto = {
  id: number;
  clientId: number;
  orderDate: string;
  total: number;
  status: 'APPROVED' | 'REJECTED' | 'PENDING';
  items: OrderItemDto[];
};

export default function OrdersPage() {
  const [orders, setOrders] = useState<OrderDto[]>([]);
  const [loading, setLoading] = useState(true);

  const load = async () => {
    setLoading(true);
    try {
      const res = await http.get<OrderDto[]>('/orders');
      setOrders(res.data);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, []);

  const color = (s: OrderDto['status']) =>
    s === 'APPROVED' ? 'success' : s === 'REJECTED' ? 'error' : 'warning';

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Pedidos
      </Typography>

      {loading ? (
        <Typography>Carregando...</Typography>
      ) : (
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>#</TableCell>
              <TableCell>Cliente</TableCell>
              <TableCell>Data</TableCell>
              <TableCell>Status</TableCell>
              <TableCell>Total</TableCell>
              <TableCell>Itens</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {orders.map((o) => (
              <TableRow key={o.id}>
                <TableCell>{o.id}</TableCell>
                <TableCell>{o.clientId}</TableCell>
                <TableCell>{o.orderDate}</TableCell>
                <TableCell>
                  <Chip label={o.status} color={color(o.status)} size="small" />
                </TableCell>
                <TableCell>R$ {Number(o.total).toFixed(2)}</TableCell>
                <TableCell>
                  {o.items?.reduce((acc, i) => acc + Number(i.quantity), 0)}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      )}
    </Box>
  );
}
