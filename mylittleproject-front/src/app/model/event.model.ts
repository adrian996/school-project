import { Time } from "@angular/common";

export class Event{
  id?: any;
  course?: {
    id?: any;
    name?: any;
    description?: any;
    points?: any;
  };
  date: {
    id?: any;
    dow?: any;
    startTime?: any;
    endTime?: any;
  };
  venue?: any;
}
