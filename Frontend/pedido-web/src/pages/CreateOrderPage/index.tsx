import { useEffect, useMemo, useState } from 'react';
import { http } from '../../api/http';
import {
  Typography,
  Box,
  Stack,
  TextField,
  MenuItem,
  Button,
  IconButton,
  Snackbar,
  Alert,
  Paper,
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';

type ClientDto = { id: number; name: string; creditLimit: number };
type ProductDto = { id: number; name: string; price: number };
type OrderItemDto = { productId: number; quantity: number; subtotal?: number };
type OrderDto = {
  id?: number;
  clientId: number;
  orderDate: string;
  total: number;
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
  items: OrderItemDto[];
};

export default function CreateOrderPage() {
  const [clients, setClients] = useState<ClientDto[]>([]);
  const [products, setProducts] = useState<ProductDto[]>([]);
  const [clientId, setClientId] = useState<number | ''>('');
  const [orderDate, setOrderDate] = useState<string>(() =>
    new Date().toISOString().slice(0, 10),
  );
  const [items, setItems] = useState<OrderItemDto[]>([
    { productId: 0, quantity: 1 },
  ]);
  const [toast, setToast] = useState<{
    open: boolean;
    msg: string;
    severity: 'success' | 'error' | 'info';
  }>({ open: false, msg: '', severity: 'success' });

  useEffect(() => {
    (async () => {
      const [c, p] = await Promise.all([
        http.get<ClientDto[]>('/clients'),
        http.get<ProductDto[]>('/products'),
      ]);
      setClients(c.data);
      setProducts(p.data);
    })();
  }, []);

  const productMap = useMemo(
    () => new Map(products.map((p) => [p.id, p])),
    [products],
  );

  const visualTotal = useMemo(
    () =>
      items.reduce((acc, it) => {
        const pr = productMap.get(it.productId);
        return acc + (pr ? pr.price * (Number(it.quantity) || 0) : 0);
      }, 0),
    [items, productMap],
  );

  const setItem = (idx: number, patch: Partial<OrderItemDto>) => {
    setItems((prev) =>
      prev.map((it, i) => (i === idx ? { ...it, ...patch } : it)),
    );
  };

  const addItem = () =>
    setItems((prev) => [...prev, { productId: 0, quantity: 1 }]);
  const removeItem = (idx: number) =>
    setItems((prev) => prev.filter((_, i) => i !== idx));

  const submit = async () => {
    if (
      !clientId ||
      !orderDate ||
      items.length === 0 ||
      items.some((i) => !i.productId || !i.quantity)
    ) {
      setToast({
        open: true,
        msg: 'Preencha cliente, data e itens válidos',
        severity: 'error',
      });
      return;
    }
    const payload: OrderDto = {
      clientId: Number(clientId),
      orderDate,
      status: 'PENDING', // back define APPROVED/REJECTED
      total: 0, // back recalcula
      items: items.map((i) => ({
        productId: Number(i.productId),
        quantity: Number(i.quantity),
      })),
    };
    try {
      const res = await http.post<OrderDto>('/orders', payload);
      setToast({
        open: true,
        msg: `Pedido #${res.data.id} ${res.data.status}`,
        severity: res.data.status === 'APPROVED' ? 'success' : 'info',
      });
      // reset leve
      setItems([{ productId: 0, quantity: 1 }]);
    } catch (e: any) {
      setToast({
        open: true,
        msg: e?.response?.data ?? 'Erro ao criar pedido',
        severity: 'error',
      });
    }
  };

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Novo Pedido
      </Typography>

      <Paper sx={{ p: 2, mb: 3 }}>
        <Stack direction="row" spacing={2}>
          <TextField
            select
            label="Cliente"
            value={clientId}
            onChange={(e) =>
              setClientId(e.target.value === '' ? '' : Number(e.target.value))
            }
            sx={{ minWidth: 260 }}
          >
            {clients.map((c) => (
              <MenuItem key={c.id} value={c.id}>
                {c.name} (Limite R$ {c.creditLimit})
              </MenuItem>
            ))}
          </TextField>

          <TextField
            type="date"
            label="Data do pedido"
            value={orderDate}
            onChange={(e) => setOrderDate(e.target.value)}
            InputLabelProps={{ shrink: true }}
          />
        </Stack>
      </Paper>

      <Paper sx={{ p: 2, mb: 2 }}>
        <Stack spacing={1}>
          {items.map((it, idx) => (
            <Stack key={idx} direction="row" spacing={2} alignItems="center">
              <TextField
                select
                label="Produto"
                sx={{ minWidth: 320 }}
                value={it.productId || 0}
                onChange={(e) =>
                  setItem(idx, { productId: Number(e.target.value) })
                }
              >
                <MenuItem value={0} disabled>
                  Selecione...
                </MenuItem>
                {products.map((p) => (
                  <MenuItem key={p.id} value={p.id!}>
                    {p.name} — R$ {p.price.toFixed(2)}
                  </MenuItem>
                ))}
              </TextField>

              <TextField
                type="number"
                label="Qtd"
                value={it.quantity}
                onChange={(e) =>
                  setItem(idx, { quantity: Number(e.target.value) })
                }
                sx={{ width: 120 }}
                inputProps={{ min: 1 }}
              />

              <Typography sx={{ minWidth: 160 }}>
                Subtotal: R${' '}
                {(() => {
                  const p = productMap.get(it.productId);
                  return (p ? p.price * (Number(it.quantity) || 0) : 0).toFixed(
                    2,
                  );
                })()}
              </Typography>

              <IconButton aria-label="remover" onClick={() => removeItem(idx)}>
                <DeleteIcon />
              </IconButton>
            </Stack>
          ))}
          <Button onClick={addItem}>+ Item</Button>
        </Stack>
      </Paper>

      <Stack
        direction="row"
        justifyContent="space-between"
        alignItems="center"
        sx={{ mb: 2 }}
      >
        <Typography variant="h6">
          Total (visual): R$ {visualTotal.toFixed(2)}
        </Typography>
        <Button variant="contained" onClick={submit}>
          Criar Pedido
        </Button>
      </Stack>

      <Snackbar
        open={toast.open}
        autoHideDuration={3000}
        onClose={() => setToast((s) => ({ ...s, open: false }))}
      >
        <Alert
          severity={toast.severity}
          onClose={() => setToast((s) => ({ ...s, open: false }))}
        >
          {toast.msg}
        </Alert>
      </Snackbar>
    </Box>
  );
}
