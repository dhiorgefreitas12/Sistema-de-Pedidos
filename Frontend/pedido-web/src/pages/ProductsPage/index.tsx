import { useEffect, useState } from 'react';
import { http } from '../../api/http';
import {
  Typography,
  Box,
  TextField,
  Button,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  Stack,
  Snackbar,
  Alert,
} from '@mui/material';

type ProductDto = { id?: number; name: string; price: number };

export default function ProductsPage() {
  const [products, setProducts] = useState<ProductDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [name, setName] = useState('');
  const [price, setPrice] = useState<number | ''>('');
  const [toast, setToast] = useState<{
    open: boolean;
    msg: string;
    severity: 'success' | 'error';
  }>({ open: false, msg: '', severity: 'success' });

  const load = async () => {
    setLoading(true);
    try {
      const res = await http.get<ProductDto[]>('/products');
      setProducts(res.data);
    } catch (e: any) {
      setToast({
        open: true,
        msg: e?.response?.data ?? 'Erro ao carregar produtos',
        severity: 'error',
      });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, []);

  const onCreate = async () => {
    if (!name || price === '' || Number(price) <= 0) {
      setToast({
        open: true,
        msg: 'Preencha nome e preço válido',
        severity: 'error',
      });
      return;
    }
    try {
      await http.post<ProductDto>('/products', { name, price: Number(price) });
      setName('');
      setPrice('');
      setToast({ open: true, msg: 'Produto criado!', severity: 'success' });
      load();
    } catch (e: any) {
      setToast({
        open: true,
        msg: e?.response?.data ?? 'Erro ao criar produto',
        severity: 'error',
      });
    }
  };

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Produtos
      </Typography>

      <Stack direction="row" spacing={2} sx={{ mb: 3 }}>
        <TextField
          label="Nome"
          value={name}
          onChange={(e) => setName(e.target.value)}
        />
        <TextField
          label="Preço"
          type="number"
          value={price}
          onChange={(e) =>
            setPrice(e.target.value === '' ? '' : Number(e.target.value))
          }
        />
        <Button variant="contained" onClick={onCreate}>
          Salvar
        </Button>
      </Stack>

      {loading ? (
        <Typography>Carregando...</Typography>
      ) : (
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell>Nome</TableCell>
              <TableCell>Preço</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {products.map((p) => (
              <TableRow key={p.id}>
                <TableCell>{p.id}</TableCell>
                <TableCell>{p.name}</TableCell>
                <TableCell>R$ {Number(p.price).toFixed(2)}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      )}

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
