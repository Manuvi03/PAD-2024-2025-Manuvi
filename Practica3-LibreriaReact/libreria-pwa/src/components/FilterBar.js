import React from 'react';

const genres = [
  "Aventuras",
  "Ciencia Ficción",
  "Histórica",
  "Novela Negra",
  "Romántica",
  "Terror",
  "Tecnología"
];

const FilterBar = ({ onFilter }) => {
  return (
    <div className="filter-bar">
      <select aria-label="Filter-bar" onChange={(e) => onFilter(e.target.value)}>
        <option value="">Todos los géneros</option>
        {genres.map((genre, index) => (
          <option key={index} value={genre}>{genre}</option>
        ))}
      </select>
    </div>
  );
};

export default FilterBar;