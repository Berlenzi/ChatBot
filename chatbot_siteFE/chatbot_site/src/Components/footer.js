import React from 'react';
import TextField from '@mui/material/TextField';
import { fontFamily, styled } from '@mui/system';
import Button from '@mui/material/Button';
import { ReactComponent as Send } from '../assets/send.svg';

const StyledTextField = styled(TextField)(() => ({
  '& .MuiOutlinedInput-root': {
    autocomplete: false,
    backgroundColor: '#2d3748', // bg-gray-800
    color: '#fff',
    '& fieldset': {
      borderColor: '#4a5568', // bg-gray-700
    },
    '&:hover fieldset': {
      borderColor: '#a0aec0', // bg-gray-400
    },
    '&.Mui-focused fieldset': {
      borderColor: '#63b3ed', // blue-400
    },
  },
  '& .MuiInputLabel-root': {
    color: '#a0aec0', // gray-400
  },
  '& .MuiInputLabel-root.Mui-focused': {
    color: '#63b3ed', // blue-400
  },
  '& .MuiInputBase-input': {
    color: '#fff',
    fontFamily: 'poppins',
  },
}));

const SendButton = styled(Button)(({ theme }) => ({
  marginLeft: theme.spacing(2),
  backgroundColor: '#63b3ed', // blue-400
  color: '#fff',
  borderRadius: '100px',
  '&:hover': {
    backgroundColor: '#4299e1', // blue-500
  },
}));

function Footer({ isSidebarOpen, onSend, idConversation }) {
  const [message, setMessage] = React.useState('');

  const token = localStorage.getItem('token');

  const handleSend = () => {
    if (message.trim() !== '') {
      onSend(message);
      setMessage('');
    }
  };

  return (
    <footer
      className={`transition-all duration-300 ${
        isSidebarOpen ? 'md:ml-20' : 'md:ml-50'
      } fixed bottom-0 left-0 w-full bg-background`}
    >
      <div className="px-4 sm:px-6 py-4 flex items-center justify-between md:justify-center">
        <StyledTextField
          id="ask-something"
          label="Ask me something"
          variant="outlined"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          fullWidth
          placeholder="Type your message here..."
          sx={{
            width: { xs: '100%', sm: 'calc(100% - 80px)', md: '60%' },
            maxWidth: '800px',
            
          }}
        />
        <SendButton
          variant="contained"
          onClick={handleSend}
          sx={{
            display: { xs: 'flex', md: 'flex' },
            width: { xs: '60px', sm: '80px' },
            minWidth: 0,
            height: '56px',
          }}
        >
          <Send />
        </SendButton>
      </div>
    </footer>
  );
}

export default Footer;
