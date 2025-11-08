import { Alert } from '@/types/alert';
import { ResourceRequest } from '@/types/request';

// Demo alerts data
export const demoAlerts: Alert[] = [
  {
    id: '1',
    message: 'Flood warning in downtown area. Water levels rising rapidly.',
    severity: 'critical',
    location: {
      type: 'Point',
      coordinates: [-74.006, 40.7128]
    },
    distance: 2500,
    createdAt: new Date(Date.now() - 2 * 60 * 60 * 1000).toISOString() // 2 hours ago
  },
  {
    id: '2',
    message: 'Heavy rainfall expected in the next 6 hours.',
    severity: 'high',
    location: {
      type: 'Point',
      coordinates: [-74.010, 40.7150]
    },
    distance: 5000,
    createdAt: new Date(Date.now() - 4 * 60 * 60 * 1000).toISOString() // 4 hours ago
  },
  {
    id: '3',
    message: 'Road closure on Main Street due to fallen tree.',
    severity: 'medium',
    location: {
      type: 'Point',
      coordinates: [-74.002, 40.7100]
    },
    distance: 1200,
    createdAt: new Date(Date.now() - 6 * 60 * 60 * 1000).toISOString() // 6 hours ago
  }
];

// Demo resource requests data
export const demoRequests: ResourceRequest[] = [
  {
    type: 'medical',
    description: 'Need medical assistance for elderly person with breathing difficulties',
    location: {
      type: 'Point',
      coordinates: [-74.005, 40.7130]
    },
    status: 'pending',
  },
  {
    type: 'food',
    description: 'Family of 4 needs food supplies, stranded due to flood',
    location: {
      type: 'Point',
      coordinates: [-74.008, 40.7140]
    },
    status: 'assigned',
  },
  {
    type: 'shelter',
    description: 'Need temporary shelter, house damaged by flood',
    location: {
      type: 'Point',
      coordinates: [-74.012, 40.7160]
    },
    status: 'completed',
  },
  {
    type: 'water',
    description: 'Need clean drinking water, local supply contaminated',
    location: {
      type: 'Point',
      coordinates: [-74.003, 40.7110]
    },
    status: 'pending',
  }
];

// Demo user data
export const demoUsers = {
  admin: {
    id: 1,
    name: 'Emergency Coordinator',
    email: 'admin@disasternet.com',
    phone: '+1-555-0100',
    role: 'AUTHORITY' as const,
    location: {
      type: 'Point',
      coordinates: [-74.006, 40.7128] // New York City
    }
  },
  volunteer: {
    id: 201,
    name: 'Sarah Johnson',
    email: 'sarah@example.com',
    phone: '+1-555-0101',
    role: 'volunteer' as const,
    location: {
      type: 'Point',
      coordinates: [-74.008, 40.7140] // Near NYC
    }
  },
  citizen: {
    id: 101,
    name: 'John Smith',
    email: 'john@example.com',
    phone: '+1-555-0102',
    role: 'citizen' as const,
    location: {
      type: 'Point',
      coordinates: [-74.005, 40.7130] // NYC area
    }
  }
};

