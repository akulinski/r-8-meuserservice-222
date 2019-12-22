export interface IRateXProfile {
  id?: number;
  ratedId?: number;
  raterId?: number;
  rateId?: number;
}

export class RateXProfile implements IRateXProfile {
  constructor(public id?: number, public ratedId?: number, public raterId?: number, public rateId?: number) {}
}
