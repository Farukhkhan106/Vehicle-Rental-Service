import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { api } from '../../axiosConfig'; // Ensure axios is configured
import UserNavbar from '../../components/Common/UserNavbar';
import '../UploadVehicle.css'; // Import styling

const UploadVehicle = () => {
  const [vehicleData, setVehicleData] = useState({
    brand: '',
    model: '',
    registrationNumber: '',
    pricePerDay: '',
    status: 'AVAILABLE',
  });

  const [files, setFiles] = useState([]);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    setVehicleData({ ...vehicleData, [e.target.name]: e.target.value });
  };

  const handleFileChange = (e) => {
    setFiles(e.target.files);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage('');

    try {
      const formData = new FormData();
      formData.append('vehicle', JSON.stringify(vehicleData)); // Send as JSON string
      for (let i = 0; i < files.length; i++) {
        formData.append('files', files[i]);
      }

      await api.post('/vehicle/add', formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      });

      setMessage('✅ Vehicle uploaded successfully!');
      setTimeout(() => navigate('/user/dashboard'), 2000);
    } catch (error) {
      console.error(error);
      setMessage('❌ Failed to upload vehicle.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="upload-vehicle-container">
      
      <h2>Upload Your Vehicle</h2>
      {message && <p className="message">{message}</p>}
      <form onSubmit={handleSubmit}>
        <input type="text" name="brand" placeholder="Brand" value={vehicleData.brand} onChange={handleChange} required />
        <input type="text" name="model" placeholder="Model" value={vehicleData.model} onChange={handleChange} required />
        <input type="text" name="registrationNumber" placeholder="Registration Number" value={vehicleData.registrationNumber} onChange={handleChange} required />
        <input type="number" name="pricePerDay" placeholder="Price Per Day (₹)" value={vehicleData.pricePerDay} onChange={handleChange} required />
        <input type="file" accept="image/*" multiple onChange={handleFileChange} required />
        <button type="submit" disabled={loading}>
          {loading ? 'Uploading...' : 'Upload Vehicle'}
        </button>
      </form>
    </div>
  );
};

export default UploadVehicle;
