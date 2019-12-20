import { Moment } from 'moment';

export interface IRate {
  id?: number;
  value?: number;
  question?: string;
  timeStamp?: Moment;
}

export class Rate implements IRate {
  constructor(public id?: number, public value?: number, public question?: string, public timeStamp?: Moment) {}
}
