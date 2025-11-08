export interface ResourceRequest {
  type: 'food' | 'water' | 'shelter' | 'medical' | 'other';
  status: 'pending' | 'assigned' | 'completed';
  description: string;
  location: {
    type: string;
    coordinates: number[];
  };
}

export interface CreateRequestRequest {
  type: 'food' | 'water' | 'shelter' | 'medical' | 'other';
  description: string;
  location: {
    type: string;
    coordinates: number[];
  };
}



