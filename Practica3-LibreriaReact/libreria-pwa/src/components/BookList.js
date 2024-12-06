import React from 'react';
import { motion } from 'framer-motion';

const BookList = ({ books }) => {
  const handleBookClick = (link) => {
    window.open(link, '_blank'); // Abre el enlace en una nueva pestaña
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
          onClick={() => handleBookClick(book.link)} // Manejador de clic
        >
          {book.imageUrl && <img src={book.imageUrl} alt={book.title} className="book-image" />}
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