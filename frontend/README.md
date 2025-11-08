# DisasterNet Frontend

A beautiful, modern React frontend for the Distributed Disaster Alert and Resource Coordination Platform built with TypeScript, TailwindCSS, and ShadCN UI components.

## Features

### 🎨 Modern UI/UX
- **Responsive Design**: Works seamlessly on desktop, tablet, and mobile devices
- **Dark/Light Theme**: Built-in theme support with CSS variables
- **Accessibility**: WCAG compliant components with proper ARIA labels
- **Beautiful Components**: ShadCN UI components with custom styling

### 🔐 Authentication System
- **Role-based Access**: Different dashboards for Citizens, Volunteers, and Admins
- **Secure Login/Register**: JWT-based authentication with cookie storage
- **Form Validation**: Real-time validation with error handling
- **User Context**: Global state management for user authentication

### 📱 Role-based Dashboards

#### 👤 Citizen Dashboard
- View nearby disaster alerts
- Request help for resources (food, water, shelter, medical aid)
- Track personal requests
- Real-time location-based alerts

#### 🤝 Volunteer Dashboard
- View nearby alerts and resource requests
- Assign themselves to help with requests
- Track assigned tasks
- Coordinate relief efforts

#### 🛡️ Admin Dashboard
- Create and broadcast disaster alerts
- Monitor all resource requests
- View system statistics
- Manage volunteer assignments

### 🗺️ Geolocation Features
- **Automatic Location Detection**: Uses browser geolocation API
- **Nearby Alerts**: Shows alerts within user's vicinity
- **Location-based Requests**: Requests are tagged with precise coordinates
- **Distance Calculation**: Shows distance to alerts and requests

### 🚨 Alert Management
- **Severity Levels**: Low, Medium, High, Critical
- **Real-time Updates**: Live alert notifications
- **Geographic Filtering**: Location-based alert filtering
- **Rich Information**: Detailed alert descriptions and locations

### 📋 Resource Request System
- **Multiple Request Types**: Food, Water, Shelter, Medical, Other
- **Status Tracking**: Pending, Assigned, Completed
- **Volunteer Assignment**: Volunteers can assign themselves to requests
- **Progress Monitoring**: Track request fulfillment

## Technology Stack

- **React 18**: Latest React with hooks and functional components
- **TypeScript**: Full type safety and better developer experience
- **TailwindCSS**: Utility-first CSS framework for rapid styling
- **ShadCN UI**: Beautiful, accessible component library
- **Axios**: HTTP client for API communication
- **Lucide React**: Beautiful icon library
- **React Router**: Client-side routing (ready for implementation)

## Project Structure

```
src/
├── components/           # Reusable UI components
│   ├── auth/            # Authentication components
│   ├── dashboard/     # Dashboard-specific components
│   └── ui/              # ShadCN UI components
├── contexts/            # React contexts
├── lib/                 # Utility functions and API client
├── pages/               # Page components
├── types/               # TypeScript type definitions
└── App.tsx              # Main application component
```

## Getting Started

### Prerequisites
- Node.js 16+ 
- npm or yarn
- Backend services running on localhost:8080

### Installation

1. **Install Dependencies**
   ```bash
   cd frontend
   npm install
   ```

2. **Start Development Server**
   ```bash
   npm start
   ```

3. **Open Browser**
   Navigate to `http://localhost:3000`

### Available Scripts

- `npm start` - Start development server
- `npm run build` - Build for production
- `npm test` - Run tests
- `npm run eject` - Eject from Create React App

## API Integration

The frontend is designed to work with the following backend services:

- **Gateway Service**: `http://localhost:8080` (API Gateway)
- **Auth Service**: `http://localhost:5001` (User authentication)
- **Alert Service**: `http://localhost:5002` (Disaster alerts)
- **Request Service**: `http://localhost:5003` (Resource requests)

### API Endpoints Used

#### Authentication
- `POST /auth/signin` - User login
- `POST /auth/signup` - User registration

#### Alerts
- `POST /alerts/new` - Create new alert (Admin only)
- `GET /alerts/nearby` - Get nearby alerts

#### Requests
- `POST /requests` - Create resource request
- `GET /requests/nearby` - Get nearby requests
- `PATCH /requests/{id}/assign` - Assign volunteer to request

## Component Architecture

### Authentication Flow
1. **AuthPage**: Landing page with login/register forms
2. **AuthContext**: Global authentication state management
3. **LoginForm/RegisterForm**: Form components with validation
4. **Protected Routes**: Role-based access control

### Dashboard System
1. **MainDashboard**: Routes to appropriate dashboard based on user role
2. **AdminDashboard**: Admin-specific features and controls
3. **CitizenDashboard**: Citizen features and request management
4. **VolunteerDashboard**: Volunteer features and assignment management

### Shared Components
1. **AlertList**: Displays alerts with severity indicators
2. **RequestList**: Shows resource requests with status
3. **CreateAlertModal**: Modal for creating new alerts
4. **CreateRequestModal**: Modal for requesting help

## Styling and Theming

### TailwindCSS Configuration
- Custom color palette for disaster management theme
- Responsive breakpoints for mobile-first design
- Utility classes for rapid development

### ShadCN UI Components
- **Button**: Multiple variants and sizes
- **Card**: Content containers with headers and footers
- **Input**: Form inputs with validation states
- **Alert**: Notification components
- **Badge**: Status indicators

### Custom Styling
- CSS variables for theme customization
- Gradient backgrounds for visual appeal
- Consistent spacing and typography
- Hover and focus states for interactivity

## State Management

### Authentication State
- User information and role
- Login/logout functionality
- Token management
- Loading states

### Application State
- Alert data and filtering
- Request data and status
- Geolocation data
- Modal states

## Error Handling

- **Form Validation**: Real-time validation with error messages
- **API Errors**: Network error handling with user feedback
- **Geolocation Errors**: Fallback for location services
- **Loading States**: Skeleton screens and spinners

## Performance Optimizations

- **Code Splitting**: Lazy loading of components
- **Memoization**: React.memo for expensive components
- **Bundle Optimization**: Tree shaking and minification
- **Image Optimization**: Responsive images and lazy loading

## Browser Support

- Chrome 90+
- Firefox 88+
- Safari 14+
- Edge 90+

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is part of the Distributed Disaster Alert and Resource Coordination Platform.

## Support

For support and questions, please contact the development team or create an issue in the repository.