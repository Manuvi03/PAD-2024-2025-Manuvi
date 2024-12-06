import React, { useState } from 'react';

const SearchBar = ({ onSearch }) => {
  const [query, setQuery] = useState('');

  const handleSearch = () => {
    onSearch(query);
  };

  return (
    <div className="search-bar">
      <div className="label">
        <input
          type="text"
          placeholder="Buscar por título"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          className="search_bar"
        />
      </div>
      <button onClick={handleSearch}>Buscar</button>
    </div>
  );
};

export default SearchBar;