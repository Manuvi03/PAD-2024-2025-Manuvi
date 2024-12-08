import React from 'react';
import { motion } from 'framer-motion';
import defaultCover from '../assets/default-cover.png'; // Importa la imagen predeterminada

const BookList = ({ books, onBookClick }) => {
  const handleBookClick = (book) => {
    if (onBookClick) {
      onBookClick(book);
    }
    window.open(book.link, '_blank'); // Abre el enlace en una nueva pestaña
  };

  return (
    <motion.ul
      className="book-list"
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      transition={{ duration: 0.5 }}
    >
      {books.map(book => (
        <motion.li
          key={book.id}
          className="book-item"
          whileHover={{ scale: 1.05 }}
          onClick={() => handleBookClick(book)} // Manejador de clic
        >
          <img
            src={book.imageUrl || defaultCover} // Usa la imagen predeterminada si no hay portada
            alt={book.title}
            className="book-image"
          />
          <div className="book-details">
            <h3>{book.title}</h3>
            <p>Autor: {book.author}</p>
            <p>Páginas: {book.pageCount}</p>
          </div>
        </motion.li>
      ))}
    </motion.ul>
  );
};

export default BookList;