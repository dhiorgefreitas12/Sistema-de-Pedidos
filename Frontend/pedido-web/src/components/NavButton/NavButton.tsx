import { Button } from '@mui/material';
import { Link as RouterLink, useLocation } from 'react-router-dom';

export function NavButton({
  to,
  children,
}: {
  to: string;
  children: React.ReactNode;
}) {
  const { pathname } = useLocation();
  const active = pathname === to || pathname.startsWith(to + '/');
  return (
    <Button
      component={RouterLink}
      to={to}
      color="inherit"
      sx={{
        opacity: active ? 1 : 0.85,
        textDecoration: active ? 'underline' : 'none',
        textUnderlineOffset: '6px',
        '&:hover': {
          backgroundColor: 'rgba(255,255,255,0.15)',
          opacity: 1,
          color: 'inherit',
        },
      }}
    >
      {children}
    </Button>
  );
}
