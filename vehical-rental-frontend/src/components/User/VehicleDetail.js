import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { api } from '../../axiosConfig';
import UserNavbar from '../../components/Common/UserNavbar';
import '../VehicleDetail.css';
const VehicleDetail = () => {
  const { id } = useParams();
  const [vehicle, setVehicle] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchVehicleDetails = async () => {
      try {
        const response = await api.get(`/vehicle/details/${id}`);
        setVehicle(response.data);
      } catch (err) {
        setError('Failed to fetch vehicle details.');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchVehicleDetails();
  }, [id]);

  if (loading) return <p>Loading vehicle details...</p>;
  if (error) return <p>{error}</p>;
  if (!vehicle) return <p>No vehicle details available.</p>;

  return (
    <div>
      <UserNavbar />
      <div className="vehicle-detail-container">
        <h1>{vehicle.brand} {vehicle.model}</h1>
        {vehicle.photoUrls?.length > 0 ? (
          <img 
            src={`http://localhost:8080${vehicle.photoUrls[0]}`} 
            alt={`${vehicle.brand} ${vehicle.model}`} 
            className="vehicle-detail-image"
          />
        ) : (
          <p>No Image Available</p>
        )}

        <div className="vehicle-detail-info">
          <p><strong>Price Per Day:</strong> ₹{vehicle.pricePerDay}</p>
          <p><strong>Status:</strong> {vehicle.status}</p>
        </div>

        <h2>Owner Details</h2>
        {vehicle.owner ? (
          <div className="owner-info">
            <p><strong>Name:</strong> {vehicle.owner.name}</p>
            <p><strong>Email:</strong> {vehicle.owner.email}</p>
            <p><strong>Phone:</strong> {vehicle.owner.phone}</p>
          </div>
        ) : (
          <p>Owner details not available.</p>
        )}
      </div>
    </div>
  );
};

export default VehicleDetail;
