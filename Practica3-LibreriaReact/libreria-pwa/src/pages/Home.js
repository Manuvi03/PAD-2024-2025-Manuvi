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
  const [recentBooks, setRecentBooks] = useState([]);

  useEffect(() => {
    const storedRecentBooks = JSON.parse(localStorage.getItem('recentBooks')) || [];
    setRecentBooks(storedRecentBooks);
  }, []);

  useEffect(() => {
    if (titleQuery || genreFilter) {
      setLoading(true);
      getBooks(titleQuery, genreFilter).then(data => {
        setBooks(data);
        setLoading(false);

        // Almacenar los últimos 5 libros consultados
        const newRecentBooks = [...data.slice(0, 5), ...recentBooks.slice(0, 4)];
        localStorage.setItem('recentBooks', JSON.stringify(newRecentBooks));
        setRecentBooks(newRecentBooks);
      });
    }
  }, [titleQuery, genreFilter]);

  const handleSearch = (query) => {
    setTitleQuery(query);
  };

  const handleFilter = (genre) => {
    setGenreFilter(genre);
  };

  return (
    <div>
      <h1>Buscador de Libros</h1>
      <SearchBar onSearch={handleSearch} />
      <FilterBar onFilter={handleFilter} />
      {loading ? <p>Cargando...</p> : <BookList books={books} />}
      <h2>Últimos 5 Libros Consultados</h2>
      <BookList books={recentBooks} />
    </div>
  );
};

export default Home;