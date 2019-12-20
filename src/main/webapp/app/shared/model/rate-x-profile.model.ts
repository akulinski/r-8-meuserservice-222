import { IUserProfile } from 'app/shared/model/user-profile.model';
import { IRate } from 'app/shared/model/rate.model';

export interface IRateXProfile {
  id?: number;
  rated?: IUserProfile;
  rater?: IUserProfile;
  rate?: IRate;
}

export class RateXProfile implements IRateXProfile {
  constructor(public id?: number, public rated?: IUserProfile, public rater?: IUserProfile, public rate?: IRate) {}
}
