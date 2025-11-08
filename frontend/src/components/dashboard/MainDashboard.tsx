import React from 'react';
import { useAuth } from '../../contexts/AuthContext';
import { Header } from './Header';
import { AdminDashboard } from './AdminDashboard';
import { CitizenDashboard } from './CitizenDashboard';
import { VolunteerDashboard } from './VolunteerDashboard';

export const MainDashboard: React.FC = () => {
  const { user } = useAuth();

  const renderDashboard = () => {
    switch (user?.role) {
      case 'AUTHORITY':
        return <AdminDashboard />;
      case 'volunteer':
        return <VolunteerDashboard />;
      case 'citizen':
        return <CitizenDashboard />;
      default:
        return <CitizenDashboard />;
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <Header />
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {renderDashboard()}
      </main>
    </div>
  );
};
