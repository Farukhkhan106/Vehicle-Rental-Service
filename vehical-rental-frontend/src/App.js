import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './components/Auth/Login';
import Register from './components/Auth/Register';
import PrivateRoute from './components/Common/ProtectedRoute';
import AdminDashboard from './components/Admin/AdminDashboard';
import UserDashboard from './components/User/UserDashboard';
import ManageVehicles from './components/Admin/ManageVehicles';
import ManageUsers from './components/Admin/ManageUsers';
import Support from './components/Admin/Support'; 
import Reports from './components/Admin/Reports'; // ✅ Reports Component Import
import Home from './components/Home';
import Profile from './components/User/Profile';
import UploadVehicle from './components/User/UploadVehicle';
import './App.css';
import VehicleDetail from './components/User/VehicleDetail';

function App() {
  return (
    <Router>
      <div>
        <Routes>
          {/* Public Routes */}
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />

          {/* Protected Routes */}
          <Route path="/admin/dashboard" element={
            <PrivateRoute role="ADMIN">
              <AdminDashboard />
            </PrivateRoute>
          } />
          <Route path="/user/dashboard" element={
            <PrivateRoute role="USER">
              <UserDashboard />
            </PrivateRoute>
          } />
          
          {/* Admin Routes */}
          <Route path="/admin/manage-vehicles" element={
            <PrivateRoute role="ADMIN">
              <ManageVehicles />
            </PrivateRoute>
          } />
          <Route path="/admin/manage-users" element={
            <PrivateRoute role="ADMIN">
              <ManageUsers />
            </PrivateRoute>
          } />
          <Route path="/admin/reports" element={
            <PrivateRoute role="ADMIN">
              <Reports />
            </PrivateRoute>
          } />
          <Route path="/admin/support" element={
            <PrivateRoute role="ADMIN">
              <Support />
            </PrivateRoute>
          } /> 
         <Route path="/user/profile" element={<Profile />} /> 
         <Route path="/user/vehicle/:id" element={<VehicleDetail />} /> 
         <Route path="/user/upload-vehicle" element={<UploadVehicle />} />   
      </Routes>
      </div>
    </Router>
  );
}

export default App;
