import React, { useState, useEffect } from 'react';
import SearchBar from '../components/SearchBar';
import FilterBar from '../components/FilterBar';
import BookList from '../components/BookList';
import { getBooks } from '../services/api';

const Home = () => {
  const [books, setBooks] = useState([]);
  const [titleQuery, setTitleQuery] = useState('');
  const [genreFilter, setGenreFilter] = useState('');
  const [loading, setLoading] = useState(false);
  const [clickedBooks, setClickedBooks] = useState([]);

  // Recuperar datos del localStorage al montar el componente
  useEffect(() => {
    const storedClickedBooks = JSON.parse(localStorage.getItem('clickedBooks')) || [];
    setClickedBooks(storedClickedBooks);
  }, []);

  // Guardar datos en el localStorage cada vez que cambien
  useEffect(() => {
    localStorage.setItem('clickedBooks', JSON.stringify(clickedBooks));
  }, [clickedBooks]);

  useEffect(() => {
    if (titleQuery || genreFilter) {
      setLoading(true);
      getBooks(titleQuery, genreFilter).then(data => {
        setBooks(data);
        setLoading(false);
      });
    }
  }, [titleQuery, genreFilter]);

  const handleSearch = (query) => {
    setTitleQuery(query);
  };

  const handleFilter = (genre) => {
    setGenreFilter(genre);
  };

  const handleBookClick = (book) => {
    const newClickedBooks = [book, ...clickedBooks.filter(b => b.id !== book.id)];
    if (newClickedBooks.length > 5) {
      newClickedBooks.pop(); // Descarta el libro más antiguo si ya hay 5 libros
    }
    setClickedBooks(newClickedBooks);
  };

  return (
    <div>
      <h1>Buscador de Libros</h1>
      <SearchBar onSearch={handleSearch} />
      <FilterBar onFilter={handleFilter} />
      {loading ? <p>Cargando...</p> : <BookList books={books} onBookClick={handleBookClick} />}
      <h2>Últimos 5 Libros Buscados:</h2>
      <BookList books={clickedBooks} />
    </div>
  );
};

export default Home;