import { Routes, Route, Link as RouterLink } from 'react-router-dom';
import { AppBar, Toolbar, Typography, Button, Box, Stack } from '@mui/material';
import ClientsPage from './pages/ClientsPage';
import ProductsPage from './pages/ProductsPage';
import OrdersPage from './pages/OrdersPage';
import CreateOrderPage from './pages/CreateOrderPage';
import { NavButton } from './components/NavButton/NavButton';

export default function App() {
  return (
    <Box
      sx={{
        minHeight: '100vh',
        width: '100vw',
        overflowX: 'hidden',
        background:
          'linear-gradient(180deg, rgba(227,242,253,1) 0%, rgba(232,244,253,1) 50%, rgba(241,248,255,1) 100%)',
      }}
    >
      <AppBar
        position="sticky"
        elevation={6}
        sx={{
          background:
            'linear-gradient(90deg, #1565c0 0%, #1976d2 50%, #1e88e5 100%)',
        }}
      >
        <Toolbar sx={{ gap: 2 }}>
          <Typography variant="h6" sx={{ flexGrow: 1, fontWeight: 700 }}>
            Sistema de Pedidos
          </Typography>
          <Stack direction="row" spacing={1}>
            <NavButton to="/clients">Clientes</NavButton>
            <NavButton to="/products">Produtos</NavButton>
            <NavButton to="/orders">Pedidos</NavButton>
            <Button
              component={RouterLink}
              to="/orders/new"
              variant="contained"
              sx={{
                backgroundColor: 'black',
                color: 'white',
                transition: 'background-color 0.3s ease',
                '&:hover': {
                  backgroundColor: '#333',
                  color: 'white',
                },
              }}
            >
              Novo Pedido
            </Button>
          </Stack>
        </Toolbar>
      </AppBar>

      <Box
        sx={{
          p: { xs: 2, sm: 3, md: 4 },
          backgroundColor: 'rgba(255,255,255,0.9)',
          borderRadius: 3,
          m: { xs: 1, md: 2 },
          boxShadow:
            '0 10px 30px rgba(25,118,210,0.15), 0 1px 3px rgba(0,0,0,0.05)',
        }}
      >
        <Routes>
          <Route path="/clients" element={<ClientsPage />} />
          <Route path="/products" element={<ProductsPage />} />
          <Route path="/orders" element={<OrdersPage />} />
          <Route path="/orders/new" element={<CreateOrderPage />} />
        </Routes>
      </Box>
    </Box>
  );
}
