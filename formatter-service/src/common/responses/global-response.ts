
export class GlobalResponse<T> {
  public static readonly SUCCESS = 'success';
  public static readonly ERROR = 'error';

  status: string;
  data: T | null;
  errors: ErrorItem[] | null;

  constructor(data?: T | null , errors?: ErrorItem[]) {
    if (errors && errors.length > 0) {
      this.status = GlobalResponse.ERROR;
      this.data = null;
      this.errors = errors;
    } else {
      this.status = GlobalResponse.SUCCESS;
      this.data = data || null;
      this.errors = null;
    }
  }

  static success<T>(data: T): GlobalResponse<T> {
    return new GlobalResponse<T>(data);
  }

  static error<T>(errors: ErrorItem[]): GlobalResponse<T> {
    return new GlobalResponse<T>(null, errors);
  }

  static errorMessage<T>(message: string): GlobalResponse<T> {
    return new GlobalResponse<T>(null, [{ message }]);
  }
}

export interface ErrorItem {
  message: string;
}