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
  const [categorizedBooks, setCategorizedBooks] = useState([]);

  useEffect(() => {
    const storedRecentBooks = JSON.parse(localStorage.getItem('recentBooks')) || [];
    setRecentBooks(storedRecentBooks);

    const storedCategorizedBooks = JSON.parse(localStorage.getItem('categorizedBooks')) || [];
    setCategorizedBooks(storedCategorizedBooks);
  }, []);

  useEffect(() => {
    if (titleQuery || genreFilter) {
      setLoading(true);
      getBooks(titleQuery, genreFilter).then(data => {
        setBooks(data);
        setLoading(false);
      });
    }
  }, [titleQuery, genreFilter]);

  useEffect(() => {
    localStorage.setItem('recentBooks', JSON.stringify(recentBooks));
  }, [recentBooks]);

  const handleSearch = (query) => {
    setTitleQuery(query);
  };

  const handleFilter = (genre) => {
    setGenreFilter(genre);
  };

  const handleCategorize = (book) => {
    const newCategorizedBooks = [book, ...categorizedBooks.filter(b => b.id !== book.id)];
    localStorage.setItem('categorizedBooks', JSON.stringify(newCategorizedBooks));
    setCategorizedBooks(newCategorizedBooks);
  };

  const handleBookClick = (book) => {
    const newRecentBooks = [book, ...recentBooks.filter(b => b.id !== book.id)];
    if (newRecentBooks.length > 5) {
      newRecentBooks.pop(); // Descarta el libro más antiguo si ya hay 5 libros
    }
    setRecentBooks(newRecentBooks);
  };

  return (
    <div>
      <h1>Buscador de Libros</h1>
      <SearchBar onSearch={handleSearch} />
      <FilterBar onFilter={handleFilter} />
      {loading ? <p>Cargando...</p> : <BookList books={books} onCategorize={handleCategorize} onBookClick={handleBookClick} />}
      <h2>Últimos 5 Libros Consultados</h2>
      <BookList books={recentBooks} />
      <h2>Libros Categorizados</h2>
      <BookList books={categorizedBooks} />
    </div>
  );
};

export default Home;