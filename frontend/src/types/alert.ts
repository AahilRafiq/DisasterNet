export interface Alert {
  id: string,
  message: string;
  severity: 'low' | 'medium' | 'high' | 'critical';
  location: {
    type: string;
    coordinates: number[];
  };
  distance?: number;
  createdAt: string;
}

export interface CreateAlertRequest {
  id: string;
  message: string;
  severity: 'low' | 'medium' | 'high' | 'critical';
  location: {
    type: string;
    coordinates: number[];
  };
  createdAt: string;
}


