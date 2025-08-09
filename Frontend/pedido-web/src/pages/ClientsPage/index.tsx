import { useQuery, useMutation } from '@tanstack/react-query';
import { http } from '../../api/http';
import {
  CircularProgress,
  Typography,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  TextField,
  Button,
  Stack,
  Snackbar,
  Alert,
  Box,
} from '@mui/material';
import { useState } from 'react';

interface Client {
  id?: number;
  name: string;
  creditLimit: number;
}

export default function ClientsPage() {
  const [name, setName] = useState('');
  const [creditLimit, setCreditLimit] = useState<number | ''>('');
  const [toast, setToast] = useState<{
    open: boolean;
    msg: string;
    sev: 'success' | 'error';
  }>({
    open: false,
    msg: '',
    sev: 'success',
  });

  const clientsQuery = useQuery<Client[]>({
    queryKey: ['clients'],
    queryFn: async () => (await http.get('/clients')).data,
  });

  const createClient = useMutation({
    mutationFn: async (payload: Omit<Client, 'id'>) =>
      (await http.post<Client>('/clients', payload)).data,
    onSuccess: () => {
      setToast({ open: true, msg: 'Cliente criado!', sev: 'success' });
      setName('');
      setCreditLimit('');
      clientsQuery.refetch();
    },
    onError: (err: string) => {
      setToast({
        open: true,
        msg: err ?? 'Erro ao criar cliente',
        sev: 'error',
      });
    },
  });

  const onSubmit = () => {
    if (!name || creditLimit === '' || Number(creditLimit) <= 0) {
      setToast({ open: true, msg: 'Preencha nome e limite > 0', sev: 'error' });
      return;
    }
    createClient.mutate({ name, creditLimit: Number(creditLimit) });
  };

  if (clientsQuery.isLoading) return <CircularProgress />;
  if (clientsQuery.error)
    return <Typography color="error">Erro ao carregar clientes</Typography>;

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Clientes
      </Typography>

      {/* Formulário de cadastro */}
      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2} sx={{ mb: 3 }}>
        <TextField
          label="Nome"
          value={name}
          onChange={(e) => setName(e.target.value)}
          fullWidth
        />
        <TextField
          label="Limite de Crédito"
          type="number"
          value={creditLimit}
          onChange={(e) =>
            setCreditLimit(e.target.value === '' ? '' : Number(e.target.value))
          }
          inputProps={{ min: 0, step: '0.01' }}
          fullWidth
        />
        <Button
          variant="contained"
          onClick={onSubmit}
          disabled={createClient.isPending}
        >
          {createClient.isPending ? 'Salvando...' : 'Salvar'}
        </Button>
      </Stack>

      <Table>
        <TableHead>
          <TableRow>
            <TableCell>ID</TableCell>
            <TableCell>Nome</TableCell>
            <TableCell>Limite de Crédito</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {clientsQuery.data?.map((c) => (
            <TableRow key={c.id}>
              <TableCell>{c.id}</TableCell>
              <TableCell>{c.name}</TableCell>
              <TableCell>R$ {Number(c.creditLimit).toFixed(2)}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>

      <Snackbar
        open={toast.open}
        autoHideDuration={3000}
        onClose={() => setToast((s) => ({ ...s, open: false }))}
      >
        <Alert
          severity={toast.sev}
          onClose={() => setToast((s) => ({ ...s, open: false }))}
        >
          {toast.msg}
        </Alert>
      </Snackbar>
    </Box>
  );
}
