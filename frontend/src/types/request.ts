export interface ResourceRequest {
  id: string; // unique identifier
  type: 'food' | 'water' | 'shelter' | 'medical' | 'other';
  status: 'pending' | 'assigned' | 'completed';
  description: string;
  citizenId?: number; // creator (citizen) id for filtering "my requests"
  assignedVolunteerId?: number; // volunteer assignment
  location: {
    type: string;
    coordinates: number[];
  };
  createdAt?: string; // optional timestamp
}

export interface CreateRequestRequest {
  type: 'food' | 'water' | 'shelter' | 'medical' | 'other';
  description: string;
  location: {
    type: string;
    coordinates: number[];
  };
  // Include the creator's id when available
  citizenId?: number;
}
