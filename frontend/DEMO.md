# DisasterNet Frontend Demo

This document provides instructions for testing the DisasterNet frontend application with demo data.

## Demo Users

The application includes three demo users for testing different roles:

### 1. Admin User
- **Email**: `admin@disasternet.com`
- **Password**: `demo123`
- **Role**: Authority/Admin
- **Features**: Create alerts, view all requests, manage system

### 2. Volunteer User
- **Email**: `sarah@example.com`
- **Password**: `demo123`
- **Role**: Volunteer
- **Features**: View nearby alerts, assign to requests, help coordinate relief

### 3. Citizen User
- **Email**: `john@example.com`
- **Password**: `demo123`
- **Role**: Citizen
- **Features**: View alerts, request help, track personal requests

## Demo Data

The application includes sample data to demonstrate functionality:

### Sample Alerts
- **Flood Warning** (Critical): Downtown area flooding
- **Heavy Rainfall** (High): Expected in next 6 hours
- **Road Closure** (Medium): Main Street blocked by fallen tree

### Sample Requests
- **Medical Aid** (Pending): Elderly person needs breathing assistance
- **Food Supplies** (Assigned): Family of 4 stranded by flood
- **Shelter** (Completed): House damaged, temporary shelter needed
- **Water** (Pending): Clean drinking water needed

## Testing the Application

### 1. Start the Application
```bash
cd frontend
npm start
```

### 2. Test Different User Roles

#### Admin Dashboard
1. Login with `admin@disasternet.com` / `demo123`
2. View system statistics
3. Create new alerts using "Create Alert" button
4. Monitor all resource requests
5. See volunteer assignments

#### Volunteer Dashboard
1. Login with `sarah@example.com` / `demo123`
2. View nearby alerts and requests
3. Click "Assign to Me" on pending requests
4. Track your assignments

#### Citizen Dashboard
1. Login with `john@example.com` / `demo123`
2. View nearby disaster alerts
3. Click "Request Help" to create new requests
4. Track your personal requests

### 3. Key Features to Test

#### Authentication
- Login with different user types
- See role-based dashboard routing
- Logout functionality

#### Alert Management
- View alerts with severity indicators
- Create new alerts (Admin only)
- See location-based filtering

#### Request Management
- Create resource requests (Citizens)
- Assign to requests (Volunteers)
- Track request status

#### Geolocation
- Allow location access when prompted
- See location-based data
- Test with different coordinates

## Demo Scenarios

### Scenario 1: Emergency Response
1. Login as Admin
2. Create a critical flood alert
3. Switch to Citizen view
4. See the alert appear
5. Request help for medical aid
6. Switch to Volunteer view
7. Assign yourself to the request

### Scenario 2: Resource Coordination
1. Login as Citizen
2. Request food supplies
3. Switch to Volunteer view
4. See the new request
5. Assign yourself to help
6. Track the assignment

### Scenario 3: System Monitoring
1. Login as Admin
2. View all active alerts
3. Monitor request statistics
4. See volunteer assignments
5. Track system health

## Technical Features Demonstrated

### UI/UX
- Responsive design for all screen sizes
- Beautiful ShadCN UI components
- Smooth animations and transitions
- Accessible form controls

### State Management
- Global authentication state
- Role-based component rendering
- Real-time data updates
- Error handling and loading states

### API Integration
- Axios HTTP client setup
- Cookie-based authentication
- Error interceptors
- Request/response handling

### Geolocation
- Browser geolocation API
- Fallback for denied permissions
- Coordinate-based filtering
- Distance calculations

## Troubleshooting

### Common Issues

1. **Location Permission Denied**
   - Application will use default coordinates
   - Demo data will still work

2. **API Connection Errors**
   - Demo mode works without backend
   - Check console for network errors

3. **Component Not Loading**
   - Check browser console for errors
   - Ensure all dependencies are installed

### Browser Compatibility
- Chrome 90+
- Firefox 88+
- Safari 14+
- Edge 90+

## Next Steps

To connect to the actual backend:

1. Start the backend services
2. Update API endpoints in `src/lib/api.ts`
3. Remove demo data imports
4. Implement real API calls in dashboard components

## Support

For issues or questions:
- Check browser console for errors
- Verify all dependencies are installed
- Ensure Node.js version is 16+



